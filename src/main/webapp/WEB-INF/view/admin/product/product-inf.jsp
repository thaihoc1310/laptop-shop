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
                <title>Product</title>
                <!-- Latest compiled and minified CSS -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
                <!-- Latest compiled JavaScript -->
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <script>
                    const imagePath = '${imagePath}';
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
                                <h1 class="mt-4">Manage Products</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item "><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item active"><a href="/admin/product">Products</a></li>
                                    <li class="breadcrumb-item active">View Detail</li>
                                </ol>
                                <!-- <div class="container"> -->
                                <div class="row d-flex justify-content-around mt-5">
                                    <div class="col-12 col-md-7 ">
                                        <h3>Product detail ${id}</h3>
                                        <hr />
                                        <div class="card">
                                            <div class="card-header">
                                                Product information
                                            </div>
                                            <ul class="list-group list-group-flush">
                                                <li class="list-group-item">Id : ${product.id}</li>
                                                <li class="list-group-item">Name : ${product.name}</li>
                                                <li class="list-group-item">Price : ${product.price}</li>
                                                <li class="list-group-item">Factory : ${product.factory}</li>
                                                <li class="list-group-item">Target : ${product.target}</li>
                                                <li class="list-group-item">ShortDescription : ${product.shortDesc}</li>
                                            </ul>
                                        </div>
                                        <div class="mb-5">
                                            <a href="/admin/product" class="btn btn-success mt-3">Back</a>
                                        </div>
                                    </div>
                                    <div class=" col-12 col-md-3 " style="margin-top: 5.2rem;">
                                        <img src="${imagePath}" style="max-height: 250px; display: 'none';"
                                            class="rounded mx-auto d-block" />
                                    </div>

                                </div>
                                <!-- </div> -->

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