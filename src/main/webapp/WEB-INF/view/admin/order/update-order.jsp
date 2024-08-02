<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="utf-8" />
                    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                    <meta name="description" content=" - Dự án laptopshop" />
                    <meta name="author" content="" />
                    <title>Order</title>
                    <link href="/css/styles.css" rel="stylesheet" />
                    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
                        crossorigin="anonymous"></script>
                </head>

                <body class="sb-nav-fixed">
                    <jsp:include page="../layout/header.jsp" />
                    <div id="layoutSidenav">
                        <jsp:include page="../layout/sidebar.jsp" />
                        <div id="layoutSidenav_content">
                            <main>
                                <div class="container-fluid px-4">
                                    <h1 class="mt-4">Manage Orders</h1>
                                    <ol class="breadcrumb mb-4">
                                        <li class="breadcrumb-item "><a href="/admin">Dashboard</a></li>
                                        <li class="breadcrumb-item active"><a href="/admin/order">Orders</a></li>
                                        <li class="breadcrumb-item active">Update</li>
                                    </ol>
                                    <div class="row mt-5">
                                        <div class="col-12 col-md-6 mx-auto">
                                            <h2>Update a order</h2>
                                            <hr />
                                            <form method="post" action="/admin/order/update">
                                                <div class="my-4">
                                                    <span>Order id = ${order1.id}</span>
                                                    <span class="mx-5">Price =
                                                        <fmt:formatNumber value="${order1.totalPrice}" type="number" />
                                                        đ
                                                    </span>
                                                </div>
                                                <div class="row mb-3">
                                                    <div class="mb-3 col-md-6">
                                                        <label class="form-label">User:</label>
                                                        <input type="text" class="form-control"
                                                            value="${order1.user.fullName}" readonly />
                                                    </div>
                                                    <div class="mb-3 col-md-6">
                                                        <label class="form-label">Status:</label>
                                                        <select class="form-select" name="status"
                                                            value="${order1.status}">
                                                            <option value="PENDING">PENDING</option>
                                                            <option value="SHIPPING">SHIPPING</option>
                                                            <option value="COMPLETE">COMPLETE</option>
                                                            <option value="CANCEL">CANCEL</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="mb-3" style="display: none;">
                                                    <input type="Number" class="form-control" name="id"
                                                        value="${order1.id}" />
                                                </div>
                                                <button type="submit" class="btn btn-warning">Confirm</button>
                                                <input type="hidden" name="${_csrf.parameterName}"
                                                    value="${_csrf.token}" />
                                            </form>
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