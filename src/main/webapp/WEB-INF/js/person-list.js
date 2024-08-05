var timer;
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
      console.log("aaa");
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
        console.log("aaa");
      },
    });
  }, 300);
}
$(".search-infor input").keyup(handleSearchInputChange);
