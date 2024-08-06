var timer;
const DEFAULT_PAGE_SIZE = 20;
var stateVar = {
  currentPage: 1,
  totalPage: 0
}

$(".search-infor input").keyup(handleSearchInputChange);

$(".search-button").on("click", function () {
  displayLoading();
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
    complete: function () {
      hideLoading();
    },
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

function handleSearchInputChange(event) {
  $(".table-containner table").children("tbody").remove();
  if (timer) clearTimeout(timer);
  timer = setTimeout(() => {
    displayLoading();
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
      complete: function () {
        hideLoading();
      },
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
    numberPageElement.click(function() {
      changePage(numberPageElement);
    });
    numberPage.append(numberPageElement);
  }
}

// change page
function changePage(currentPageElement) {
  displayLoading();
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
    complete: function () {
      hideLoading();
    },
  });
}

// click previousBtn
$("#previousBtn").click(function() {
  if (stateVar.currentPage - 1 > 0) {
    changePage($("#pageIndex-" + (stateVar.currentPage - 1)));
  }
});

// click nextBtn
$("#nextBtn").click(function() {
  if (stateVar.currentPage + 1 <= stateVar.totalPage) {
    changePage($("#pageIndex-" + (stateVar.currentPage + 1)));
  }
});

$("#exportData").click(function() {
  displayLoading();
  $.ajax({
    url: "http://localhost:8080/api/users/export-excel",
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify({
      name: $(".search-button-name input").val(),
      phone: $(".search-button-phone input").val(),
      key: $(".search-infor input").val(),
    }),
    xhrFields:{
      responseType: 'blob'
    },
    success: function (result) {
      console.log(result)
      const downloadUrl = URL.createObjectURL(result);
      const a = document.createElement("a");
      a.href = downloadUrl;
      a.download = "user-data-export.xls";
      document.body.appendChild(a);
      a.click();
    },
    error: function (data, status, err) {},
    complete: function () {
      hideLoading();
    },
  });
});

// loading
function displayLoading() {
  $("#loading").addClass("display");
  $(".loading-container").addClass("display");
}

function hideLoading() {
  $("#loading").removeClass("display");
  $(".loading-container").removeClass("display");
}