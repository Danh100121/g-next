var timer;
const DEFAULT_PAGE_SIZE = 20;
var stateVar = {
  currentPage: 1,
  totalPage: 0,
};

$(".search-infor input").keyup(searchDebounce);

$(".search-button").on("click", function loadData() {
  $.ajax({
    url: "http://localhost:8080/api/users",
    method: "post",
    data: JSON.stringify({
      name: $(".search-button-name input").val(),
      phone: $(".search-button-phone input").val(),
    }),
    contentType: "application/json",
    success: function (response, status, xhr) {
      searchData(response.data);
      $("#total").text("Total: " + response.pagination.totalRecord);
      createElementPage(response.pagination.totalPage);
      stateVar.currentPage = 1;
      stateVar.totalPage = response.pagination.totalPage;
    },
    error: function (data, status, err) {},
    complete: function () {},
  });
});

function searchData(data) {
  $(".table-containner table").children("tbody").remove();
  var tbody = "<tbody>";
  data.forEach((element) => {
    tbody += `<tr>
        <td>${element.id}</td>
        <td>${element.name}</td>
        <td>${element.email}</td>
        <td>${element.birthDate}</td>
        <td>${element.gender}</td>
        <td>${element.phone}</td>
        <td>${element.address}</td>
        <td>${element.team}</td>
        <td>${element.status}</td>
    </tr>`;
  });
  tbody += " </tbody>";
  $(".table-containner table").append(tbody);
}

function searchDebounce(event) {
  $(".table-containner table").children("tbody").remove();
  if (timer) clearTimeout(timer);
  timer = setTimeout(() => {
    $.ajax({
      url: "http://localhost:8080/api/users",
      method: "post",
      data: JSON.stringify({
        key: $(".search-infor input").val(),
      }),
      contentType: "application/json",
      success: function (response, status, xhr) {
        searchData(response.data);
        $("#total").text("Total: " + response.pagination.totalRecord);
        createElementPage(response.pagination.totalPage);
        stateVar.currentPage = 1;
        stateVar.totalPage = response.pagination.totalPage;
      },
      error: function (data, status, err) {},
      complete: function () {},
    });
  }, 300);
}

function createElementPage(totalPage) {
  const numberPage = $(".number_page");
  numberPage.empty();
  for (let i = 1; i <= totalPage; i++) {
    let numberPageElement = $("<a></a>");
    numberPageElement.addClass("pageIndex");
    numberPageElement.attr("id", "pageIndex-" + i);
    if (i === 1) {
      numberPageElement.addClass("active");
    }
    numberPageElement.text(i);
    numberPageElement.click(function () {
      changePage(numberPageElement);
    });
    numberPage.append(numberPageElement);
  }
}

// change page
function changePage(currentPageElement) {
  $.ajax({
    url: `http://localhost:8080/api/users?pageSize=${DEFAULT_PAGE_SIZE}&pageNum=${currentPageElement[0].textContent}`,
    method: "post",
    data: JSON.stringify({
      name: $(".search-button-name input").val(),
      phone: $(".search-button-phone input").val(),
      key: $(".search-infor input").val(),
    }),
    contentType: "application/json",
    success: function (response, status, xhr) {
      searchData(response.data);

      $(".pageIndex").removeClass("active");
      currentPageElement.eq(0).addClass("active");
      stateVar.currentPage = Number(currentPageElement[0].textContent);
    },
    error: function (data, status, err) {},
    complete: function () {},
  });
}

// click previousBtn
$("#previousBtn").click(function () {
  if (stateVar.currentPage - 1 > 0) {
    changePage($("#pageIndex-" + (stateVar.currentPage - 1)));
  }
});

// click nextBtn
$("#nextBtn").click(function () {
  if (stateVar.currentPage + 1 <= stateVar.totalPage) {
    changePage($("#pageIndex-" + (stateVar.currentPage + 1)));
  }
});

// click "Create"
$(".button-dowload button").click(function () {
  $("#create-person-modal").load(
    "../html/create-person.html .container",
    function () {
      $("<link/>", {
        rel: "stylesheet",
        type: "text/css",
        href: "../css/create-person.css",
      }).appendTo("head");

      $("<script/>", {
        src: "../js/create-person.js",
      }).appendTo("body");

      $("#create-person-modal").css("display", "block");

      $(".btn-close").click(function () {
        $("#create-person-modal").css("display", "none");
      });

      // $(window).click(function (event) {
      //   if ($(event.target).is("#create-person-modal")) {
      //     $("#create-person-modal").css("display", "none");
      //   }
      // });
    }
  );
});
