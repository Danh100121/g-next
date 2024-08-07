console.log("aaaaa");
function loadData() {
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
}

$("#addUser").on("click", function () {
  console.log($("#name").val());

  var userData = {
    id: -1,
    name: $("#name1").val(),
    email: $("#email").val(),
    birthDate: $("#birthdate").val(),
    phone: $("#phone").val(),
    address: $("#address").val(),
    gender: $(".gender select").val(),
    team: $(".team select").val(),
    status: $(".status select").val(),
  };

  $.ajax({
    url: "http://localhost:8080//api/users/upsert",
    method: "post",
    data: JSON.stringify(userData),
    contentType: "application/json",
    success: function (response) {
      $("#create-person-modal").css("display", "none");
      // loadData();
    },
    error: function (xhr, status, error) {
      alert(JSON.parse(xhr.responseText).params.errors.join("\n"));
      console.log(JSON.parse(xhr.responseText).params.errors.join("\n"));
    },
  });
});
