from rest_framework.decorators import api_view, APIView
from rest_framework.response import Response
from django.http.response import JsonResponse
from .models import *
from django.views.decorators.csrf import csrf_exempt
from .serializers import *
import json  


@api_view(['GET'])
def adminList(request):
    admins = Admin.objects.all()
    adminsSer = AdminSerializer(admins, many=True)
    return Response(adminsSer.data)

@api_view(['GET'])
def getOneSellerProdcts(request, seller_id):
    products = Product.objects.filter(seller = seller_id)
    productsSer = ProductSerializer(products, many = True)
    return Response(productsSer.data)

@api_view(['GET'])
def getOneCategoryProdcts(request, category_id):
    products = Product.objects.filter(category = category_id)
    productsSer = ProductSerializer(products, many = True)
    return Response(productsSer.data)

class Sellers(APIView):
    def get(self, request):
        sellers = Seller.objects.all()
        serializer = SellerSerializer(sellers, many = True)
        return Response(serializer.data)
    
    def post(self, request):
        serializer = SellerSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response({
            "error":"invalid seller"
        })


@api_view(['GET'])   
def getAllClient(request):
    clients = Client.objects.all()
    serializer = ClientSerializer(clients, many=True)
    return Response(serializer.data)

class Clients(APIView):
    def get(self, request):
        clients = Client.objects.all()
        serializer = ClientSerializer(clients, many=True)
        return Response(serializer.data)
    
    def post(self, request):
        serializer = ClientSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response({
            "error":"invalid client"
        })

        
class SellerById(APIView):

    def get(self, request, seller_id):
        try:
            seller = Seller.objects.get(id = seller_id)
            sellerSer = SellerSerializer(seller, many=False)
            return Response(sellerSer.data)
        except Seller.DoesNotExist as e:
            return JsonResponse({"error": str(e)})

    def delete(self, request, seller_id):
        try:
            seller = Seller.objects.get(id = seller_id)
        except Seller.DoesNotExist as e:
            return JsonResponse({"error": str(e)})
        
        seller.delete()
        return JsonResponse({"delete": "succesful"})
    
    def put(self, request, seller_id):
        try:
            seller = Seller.objects.get(id = seller_id)
        except Seller.DoesNotExist as e:
            return JsonResponse({"error": str(e)})

        data = json.loads(request.body)

        username = data.get('username', '')
        email = data.get('email','')
        company_name = data.get('company_name','')
        password = data.get('password','')

        seller.username = username
        seller.email = email
        seller.company_name = company_name
        seller.password = password
        seller.save()

        serializer = SellerSerializer(seller, many=False)

        return Response(serializer.data)
    

class ClientById(APIView):

    def get(self, request, client_id):
        try:
            client = Client.objects.get(id = client_id)
            clientSer = ClientSerializer(client, many = False)
            return Response(clientSer.data)
        except Client.DoesNotExist as e:
            return Response({"error": str(e)})

    def delete(self, request, client_id):
        try:
            client = Client.objects.get(id = client_id)
        except Client.DoesNotExist as e:
            return JsonResponse({"error": str(e)})
        client.delete()
        return JsonResponse({"delete": "succesful"})
    
    def put(self, request, client_id):
        try:
            client = Client.objects.get(id = client_id)
        except Seller.DoesNotExist as e:
            return JsonResponse({"error": str(e)})

        data = json.loads(request.body)

        username = data.get('username', '')
        email = data.get('email','')
        city = data.get('city','')
        password = data.get('password','')

        client.username = username
        client.email = email
        client.city = city
        client.password = password
        client.save()

        serializer = ClientSerializer(client, many=False)

        return Response(serializer.data)
    

class Categoties(APIView):
    def get(self, request):
        categories = Category.objects.all()
        categoriesSer = CategorySerializer(categories, many = True)
        return Response(categoriesSer.data)
    
    def post(self, request):
        serializer = CategorySerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response({
            "error":"invalid category"
        })


class Products(APIView):
    def get(self, request):
        products = Product.objects.all()
        productSer = ProductSerializer(products, many = True)
        return Response(productSer.data)
    
    def post(self, request):
        serializer = ProductSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response({
            "error":"invalid category"
        })
    
    
class RatingsView(APIView):
    def get(self, request):
        rating = Rating.objects.all()
        ratingSer = RatingSerializer(rating, many = True)
        return Response(ratingSer.data)
    
    def post(self, request):
        serializer = RatingSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response({
            "error":"invalid category"
        })
    
class CategoryById(APIView):
    def get(self, request, category_id):
        try:
            category = Category.objects.get(id = category_id)
            categorySer = CategorySerializer(category, many=False)
            return Response(categorySer.data)
        except Category.DoesNotExist as e:
            return JsonResponse({"error": str(e)})

    def delete(self, request, category_id):
        try:
            category = Category.objects.get(id = category_id)
        except Category.DoesNotExist as e:
            return JsonResponse({"error": str(e)})
        
        category.delete()
        return JsonResponse({"delete": "succesful"})
    
    def put(self, request, category_id):
        try:
            category = Category.objects.get(id = category_id)
        except Category.DoesNotExist as e:
            return JsonResponse({"error": str(e)})

        data = json.loads(request.body)

        name = data.get('name', '')
        description = data.get('description','')
        imageURL = data.get('imageURL','')

        category.name = name
        category.description = description
        category.imageURL = imageURL

        category.save()

        serializer = CategorySerializer(category, many=False)

        return Response(serializer.data)
    


@api_view(['GET'])
def CategoryByName(request, name):
    cat = Category.objects.filter(name = name)[0]
    catSer = CategorySerializer(cat, many = False)
    return Response(catSer.data)

@api_view(['GET'])
def SellerByName(request, name):
    cat = Seller.objects.filter(company_name = name)[0]
    catSer = SellerSerializer(cat, many = False)
    return Response(catSer.data)

@api_view(['GET'])
def filterComCat(request, company_id, category_id):
    companyList = Product.objects.filter(seller_id = company_id)
    categoryList = companyList.filter(category_id = category_id)
    prodSer = ProductSerializer(categoryList, many = True)
    return Response(prodSer.data)

class ProductById(APIView):
    def get(self, request, product_id):
        try:
            product = Product.objects.get(id = product_id)
            productSer = ProductSerializer(product, many=False)
            return Response(productSer.data)
        except Product.DoesNotExist as e:
            return JsonResponse({"error": str(e)})

    def delete(self, request, product_id):
        try:
            product = Product.objects.get(id = product_id)
        except Product.DoesNotExist as e:
            return JsonResponse({"error": str(e)})
        
        product.delete()
        return JsonResponse({"delete": "succesful"})
    
    def put(self, request, product_id):
        try:
            product = Product.objects.get(id = product_id)
        except Product.DoesNotExist as e:
            return JsonResponse({"error": str(e)})

        data = json.loads(request.body)

        name = data.get('name', '')
        description = data.get('description','')
        imageURL = data.get('imageURL','')
        price = data.get('price','')
        quantity = data.get('quantity','')
        category = data.get('category','')
        seller = data.get('seller','')



        product.name = name
        product.description = description
        product.imageURL = imageURL
        product.price = price
        product.quantity = quantity
        product.category_id = category
        product.seller_id = seller

        product.save()

        serializer = ProductSerializer(product, many=False)

        return Response(serializer.data)


@api_view(['GET'])
def popularProducts(request):
    pro = Product.objects.all()[:4]
    proSer = ProductSerializer(pro, many = True)
    return Response(proSer.data)


class RatingById(APIView):

    def get(self, request, product_id):
        try:
            rating = Rating.objects.filter(product_id = product_id)
            ratingSer = RatingSerializer(rating, many=True)
            return Response(ratingSer.data)
        except Rating.DoesNotExist as e:
            return JsonResponse({"error": str(e)})

    def delete(self, request, seller_id):
        try:
            seller = Seller.objects.get(id = seller_id)
        except Seller.DoesNotExist as e:
            return JsonResponse({"error": str(e)})
        
        seller.delete()
        return JsonResponse({"delete": "succesful"})
    
    def put(self, request, seller_id):
        try:
            seller = Seller.objects.get(id = seller_id)
        except Seller.DoesNotExist as e:
            return JsonResponse({"error": str(e)})

        data = json.loads(request.body)

        username = data.get('username', '')
        email = data.get('email','')
        company_name = data.get('company_name','')
        password = data.get('password','')

        seller.username = username
        seller.email = email
        seller.company_name = company_name
        seller.password = password
        seller.save()

        serializer = SellerSerializer(seller, many=False)

        return Response(serializer.data)
    


class LikeProduct(APIView):
    def get(self, request):
        likes = LikeProducts.objects.all()
        likesSer = LikedSerializer(likes, many = True)
        return Response(likesSer.data)
    
    def post(self, request):
        serializer = LikedSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response({
            "error":"invalid category"
        })
    
class LikeProductById(APIView):

    def get(self, request, client_id):
        try:
            likes = LikeProducts.objects.filter(client_id = client_id)
            likesSer = LikedSerializer(likes, many=True)
            return Response(likesSer.data)
        except LikeProduct.DoesNotExist as e:
            return JsonResponse({"error": str(e)})
        
    def delete(self, request, product_id):
        try:
            like = LikeProducts.objects.get(product_id = product_id)
        except LikeProducts.DoesNotExist as e:
            return JsonResponse({"error": str(e)})
        
        like.delete()
        return JsonResponse({"delete": "succesful"})
    
        
class Carts(APIView):
    def get(self, request):
        cart = Cart.objects.all()
        cartSer = CartSerializer(cart, many = True)
        return Response(cartSer.data)
    
    def post(self, request):
        serializer = CartSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response({
            "error":"invalid category"
        })
    
class CartById(APIView):

    def get(self, request, client_id):
        try:
            cart = Cart.objects.filter(client_id = client_id)
            cartSer = CartSerializer(cart, many=True)
            return Response(cartSer.data)
        except Cart.DoesNotExist as e:
            return JsonResponse({"error": str(e)})
        
    def delete(self, request, cart_id):
        try:
            cart = Cart.objects.get(id = cart_id)
        except Cart.DoesNotExist as e:
            return JsonResponse({"error": str(e)})
        
        cart.delete()
        return JsonResponse({"delete": "succesful"})
    
    def put(self, request, cart_id):
        try:
            cart = Cart.objects.get(id = cart_id)
        except Cart.DoesNotExist as e:
            return JsonResponse({"error": str(e)})
        
        data = json.loads(request.body)
        check_order = data.get('check_order', '')
        cart.check_order = check_order
        cart.save()
        serializer = CartSerializer(cart, many=False)

        return Response(serializer.data)
    
class CartByIdSeller(APIView):

    def get(self, request, seller_id):
        try:
            cart = Cart.objects.filter(seller_id = seller_id)
            cartSer = CartSerializer(cart, many=True)
            return Response(cartSer.data)
        except Cart.DoesNotExist as e:
            return JsonResponse({"error": str(e)})