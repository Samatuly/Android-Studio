from django.urls import path
from api import views

urlpatterns = [ 
    # seller
    path('sellers/', views.Sellers.as_view()),
    path('createSeller/', views.Sellers.as_view()),
    path('SellerById/<int:seller_id>/', views.SellerById.as_view()),
    path('SellerById/<int:seller_id>/products/', views.getOneSellerProdcts),
    path('SellerByName/<str:name>/', views.SellerByName),


    #filter
    path('filter/<int:company_id>/<int:category_id>/', views.filterComCat),

    # client
    path('clients/', views.Clients.as_view()),
    path('createClient/', views.Clients.as_view()),
    path('clientById/<int:client_id>/', views.ClientById.as_view()),

    # admin
    path('admins/', views.adminList),

    # category
    path('categories/<int:category_id>/products', views.getOneCategoryProdcts),
    path('categories/', views.Categoties.as_view()),
    path('categotyByName/<str:name>/', views.CategoryByName),
    path('categotyByID/<int:category_id>/', views.CategoryById.as_view()),

    # products
    path('products/', views.Products.as_view()),
    path('popularProducts/', views.popularProducts),
    path('productById/<int:product_id>/', views.ProductById.as_view()),

    # rating
    path('ratings/', views.RatingsView.as_view()),
    path('ratings/<int:product_id>/', views.RatingById.as_view()),

    # likedProducts
    path('likes/', views.LikeProduct.as_view()),
    path('likes/<int:client_id>/', views.LikeProductById.as_view()),
    path('likesDel/<int:product_id>/', views.LikeProductById.as_view()),

    # cart
    path('carts/', views.Carts.as_view()),
    path('carts/<int:client_id>/', views.CartById.as_view()),
    path('cartsSeller/<int:seller_id>/', views.CartByIdSeller.as_view()),
    path('cartsDel/<int:cart_id>/', views.CartById.as_view()),
    path('cartsUpdate/<int:cart_id>/', views.CartById.as_view()),
]
