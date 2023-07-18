from django.contrib import admin
from .models import *

admin.site.register(Seller)

admin.site.register(Client)

admin.site.register(Admin)

admin.site.register(Product)
admin.site.register(Category)

admin.site.register(Rating)
admin.site.register(LikeProducts)
admin.site.register(Cart)