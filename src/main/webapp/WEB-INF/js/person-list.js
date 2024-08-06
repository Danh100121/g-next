var timer;
var currentQueryData = {
  page: 0,
  size: 12,
  data: {
    type: "service",
  },
};
var totalPage = 0;
let retryCount = 0;
const maxReties = 4;
const retryDelay = 200;
$(".search-button").on("click", function () {
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
    },
    error: function (data, status, err) {},
    complete: function () {
      console.log("error");
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
      },
      error: function (data, status, err) {},
      complete: function () {
        console.log("error");
      },
    });
  }, 300);
}
$(".search-infor input").keyup(handleSearchInputChange);

function createElementPage(totalPage) {
  const numberPage = document.querySelector(".number_page");
  numberPage.innerHTML = "";
  for (let i = 1; i <= totalPage; i++) {
    let numberPageElement = document.createElement("a");
    numberPageElement.classList.add("pageIndex");
    numberPageElement.setAttribute("id", "pageIndex-" + (i - 1));
    if (i == 1) {
      numberPageElement.classList.add("active");
    }
    numberPageElement.textContent = i;
    numberPageElement.onclick = function () {
      changePage(numberPageElement);
    };
    numberPage.appendChild(numberPageElement);
  }
}

// xu ly chuyen trang
function changePage(currentPageElement) {
  let temp = currentQueryData.page;
  currentQueryData.page = currentPageElement.textContent - 1;
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
      createElementPage(totalPage);
      $("#total").text("Total: " + response.pagination.totalRecord);
    },
    error: function (data, status, err) {},
    complete: function () {
      console.log("error");
    },
  })
    .then((response) => {
      if (response.code == 200) {
        const productsContainner = document.querySelector(
          ".products_containner"
        );
        productsContainner.innerHTML = "";
        const numberPageElements = document.getElementsByClassName("pageIndex");
        Array.from(numberPageElements).forEach((elment) => {
          elment.classList.remove("active");
        });
        currentPageElement.classList.add("active");
        response.data.items.forEach((product) => {
          createProductDetails(product);
        });
      } else {
        currentQueryData.page = temp;
        alert("Server have an error!!! Please try later!!.");
      }
    })
    .finally(() => hideLoading());
}
