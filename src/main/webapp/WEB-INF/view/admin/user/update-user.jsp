<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <meta name="description" content="Hỏi Dân IT - Dự án laptopshop" />
                <meta name="author" content="Hỏi Dân IT" />
                <link href="/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
                <title>User</title>
                <!-- Latest compiled and minified CSS -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
                <!-- Latest compiled JavaScript -->
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <script>
                    $(document).ready(() => {
                        const avatarFile = $("#avatarFile");
                        avatarFile.change(function (e) {
                            const imgURL = URL.createObjectURL(e.target.files[0]);
                            $("#avatarPreview").attr("src", imgURL);
                            $("#avatarPreview").css({ "display": "block" });
                        });
                    });

                    var imagePath = '${imagePath}';
                    if (imagePath !== "") {
                        $("#avatarPreview").css({ "display": "block" });
                    }
                </script>
            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />
                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <div class="container-fluid px-4">
                                <h1 class="mt-4">Manage Users</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item "><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Users</li>
                                </ol>
                                <div class="row">
                                    <div class="col-12 col-md-6 mx-auto">
                                        <h1>Update a user</h1>
                                        <hr />
                                        <form:form method="post" action="/admin/user/update"
                                            modelAttribute="user-update" class="row" enctype="multipart/form-data">
                                            <div class="mb-3" style="display: none;">
                                                <label class="form-label">Id:</label>
                                                <form:input type="Number" class="form-control" path="id" />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Email:</label>
                                                <form:input type="email" class="form-control" path="email"
                                                    disabled="true" />
                                            </div>
                                            <div class="mb-3 col-md-6">
                                                <label class="form-label">Phone Number:</label>
                                                <form:input type="text" class="form-control" path="phone" />
                                            </div>
                                            <div class="mb-3 col-md-6">
                                                <label class="form-label">Full Name:</label>
                                                <form:input type="text" class="form-control" path="fullName" />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Address:</label>
                                                <form:input type="text" class="form-control" path="address" />
                                            </div>
                                            <div class="mb-3 col-md-6">
                                                <label for="avatarFile" class="form-label">Avatar:</label>
                                                <input id="avatarFile" class="form-control" type="file"
                                                    accept=".png, .jpg, .jpeg" name="imageFile" />
                                            </div>
                                            <div class="mb-3 col-md-6">
                                                <label class="form-label">Role:</label>
                                                <form:select class="form-select" path="role.name">
                                                    <form:option value="ADMIN">ADMIN</form:option>
                                                    <form:option value="USER">USER</form:option>
                                                </form:select>
                                            </div>
                                            <div class="mb-3 col-12">
                                                <img src="${imagePath}" style="max-height: 250px; display: none;"
                                                    alt="avatar" id="avatarPreview" />
                                            </div>
                                            <div class="mb-5 col-12">
                                                <button type="submit" class="btn btn-warning">Update</button>
                                            </div>
                                        </form:form>
                                    </div>
                                </div>
                            </div>
                        </main>
                        <jsp:include page="../layout/footer.jsp" />
                    </div>
                </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
                <script src="/js/scripts.js"></script>
            </body>

            </html>