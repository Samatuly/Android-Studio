package com.example.remoteshop.backend

import android.util.Log
import com.example.remoteshop.backend.products.*
import com.example.remoteshop.backend.users.Admin
import com.example.remoteshop.backend.users.Client
import com.example.remoteshop.backend.users.Seller
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface api_services {

    @GET("clients")
    fun getAllClients(): Call<List<Client>>


    @POST("createClient/")
    suspend fun createClient(
        @Body client: Client
    ): Response<ResponseBody>

    @PUT("clientById/{id}/")
    fun updateClient(@Path("id") id:Int,  @Body client: Client): Call<Void>

    @GET("clientById/{id}")
    fun getClientById(@Path("id") id:Int): Call<Client>

    @GET("sellers")
    fun getAllSellers(): Call<List<Seller>>

    @POST("createSeller/")
    suspend fun createSeller(
        @Body seller: Seller
    ): Response<ResponseBody>

    @GET("admins")
    fun getAllAdmins(): Call<List<Admin>>

    @GET("SellerById/{id}")
    fun getSellerById(@Path("id") id:Int): Call<Seller>

    @GET("SellerByName/{name}")
    fun getCompanyName(@Path("name") name:String): Call<Seller>

    @PUT("SellerById/{id}/")
    fun updateSeller(@Path("id") id:Int,  @Body seller: Seller): Call<Void>

    @DELETE("SellerById/{id}/")
    fun deleteSeller(@Path("id") id:Int): Call<ResponseBody>

    @DELETE("clientById/{id}/")
    fun deleteClient(@Path("id") id:Int): Call<ResponseBody>


    @GET("products")
    fun getAllProducts(): Call<List<Product>>

    @GET("popularProducts")
    fun getPopularProducts(): Call<List<Product>>

    @GET("categories/{cat_id}/products")
    fun getProductsByCategory(@Path("cat_id") cat_id:Int): Call<List<Product>>

    @GET("productById/{id}")
    fun getProductById(@Path("id") id:Int): Call<Product>

    @PUT("productById/{id}/")
    fun updateProduct(@Path("id") id:Int,  @Body product: Product): Call<Void>

    @DELETE("productById/{id}/")
    fun deleteProduct(@Path("id") id:Int): Call<ResponseBody>

    @GET("SellerById/{seller}/products")
    fun getSelletProducts(@Path("seller") id:Int): Call<List<Product>>

    @POST("products/")
    suspend fun addProduct(
        @Body product: Product
    ): Response<ResponseBody>

    @GET("categories")
    fun getAllCategories(): Call<List<Category>>

    @GET("categotyByName/{name}")
    fun getCategory(@Path("name") name:String): Call<Category>


    //ratings
    @GET("ratings/{product_id}")
    fun getProductRating(@Path("product_id") product_id:Int): Call<List<Rating>>

    @POST("ratings/")
    suspend fun addRating(
        @Body rating: Rating
    ): Response<ResponseBody>


    //likes
    @GET("likes")
    fun getAllLikes(): Call<List<LikeProduct>>

    @GET("likes/{client_id}")
    fun getClientLikes(@Path("client_id") client_id: Int): Call<List<LikeProduct>>

    @POST("likes/")
    suspend fun addLikes(
        @Body like: LikeProduct
    ): Response<ResponseBody>

    @DELETE("likesDel/{id}/")
    fun deleteFromLikes(@Path("id") id:Int): Call<ResponseBody>


    //filter
    @GET("filter/{idCom}/{idCat}")
    fun getComCat(@Path("idCom") idCom:Int,
                  @Path("idCat") idCat:Int): Call<List<Product>>

    //cart
    @GET("carts/{client_id}")
    fun getClientCart(@Path("client_id") client_id: Int): Call<List<Cart>>

    @POST("carts/")
    suspend fun addCarts(
        @Body cart: Cart
    ): Response<ResponseBody>

    @DELETE("cartsDel/{id}/")
    fun deleteFromCart(@Path("id") id:Int): Call<ResponseBody>

    @GET("cartsSeller/{seller_id}")
    fun getSellerOrder(@Path("seller_id") seller_id: Int): Call<List<Cart>>

    @PUT("cartsUpdate/{id}/")
    fun updateCart(@Path("id") id:Int,  @Body cart: Cart): Call<Void>
}