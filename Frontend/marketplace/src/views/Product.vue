<template>
  <div>
    <AppHeader />
  </div>
  <div class="close-button">
    <el-icon @click="handleCloseClick">
      <Close />
    </el-icon>
  </div>
  <div class="tabs-container">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="Description" name="description" />
      <el-tab-pane label="Review" name="review" />
    </el-tabs>
  </div>
  <div class="product-view">
    <div>
      <div v-if="product.discounts && product.discounts.length > 0" class="discount-tags">
        <el-tag
          v-for="discount in product.discounts"
          :key="discount.id"
          type="success"
          style="margin-left: 8px;"
        >
          RM{{ discount.amount }} off
        </el-tag>
      </div>
      <div v-if="product.image">
        <el-image
          class="product-image"
          :src="getImageUrl(product.image)"
          fit="contain"
        />
      </div>
      <div v-else class="no-image">
        <el-icon><PictureFilled /></el-icon>
      </div>
    </div>    
    <div class="divider"></div>
    
    <h1 class="product-name">{{ product.name }}</h1>
    
    <div>
      <el-descriptions column="2" class="product-details-descriptions">
        <el-descriptions-item label="Product Details">{{ product.description }}</el-descriptions-item>
        <el-descriptions-item label="Weight">{{ product.weight }} g</el-descriptions-item>
        <el-descriptions-item label="Materials">{{ product.materials }}</el-descriptions-item>
        <el-descriptions-item label="Stock">{{ product.quantity }}</el-descriptions-item>
        <el-descriptions-item label="Certifications">{{ product.certifications }}</el-descriptions-item>
        <el-descriptions-item label="Price">
          <span>RM {{ product.discountedPrice }}</span>
          <span style="margin-left: 8px">RM{{ product.price }}</span>
        </el-descriptions-item>
      </el-descriptions>
    </div>  

    <div class="cart-controls">
      <el-input-number 
        v-model="selectedQuantity"
        :min="1"
        :max="product.quantity"
        controls-position="right"
        :step="1"
        class="quantity-input"
      />
      <el-button type="success" class="add-to-cart-button" @click="addToCart">
        Add to Cart
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus';
import AppHeader from '@/components/AppHeader.vue';
import { useAllDataStore } from '@/stores';
import { ref, onMounted, getCurrentInstance, computed } from 'vue'
import { useRouter } from 'vue-router'

const {proxy} = getCurrentInstance()

const router = useRouter()

const activeTab = ref('description')

const store = useAllDataStore();

const getImageUrl = (user)=>{
    return new URL(`../assets/${user}`,import.meta.url).href
}

const product = ref({
  id: null,
  name: '',
  description: '',
  price: '',
  image: '',
  quantity: 1,
  category: '',
  status: '',
  materials: '',
  certifications: '',
  discounts: [{
    id: null,
    name: '',
    description: '',
    amount: null,
    startDate: '',
    expiryDate: ''
  }],
  weight: null,
  sellerId: null
})

const selectedQuantity = ref(1);

const handleTabClick = (tab) => {
  if (tab.props.name === 'review') {
    router.push('/review')
  }
}

const currentProductId = computed(() => store.state.selectedProductId);

const handleCloseClick = () => {
    router.push('/main')
}

const getProductById = async () => {
  try{
    const response = await proxy.$api.getProductById(currentProductId.value);
    product.value = response.data.data;
  } catch (error) {
    console.error('Error retrieving product:', error);
  }
}


const customerId = computed(()=>store.state.userId);

const cart = ref({
  customerId: customerId.value
})

const addToCart = async () => {
  try{
    const response = await proxy.$api.addCart(cart.value);
    const cartId = response.data.data.id;

    const cartItemDto = {
      cartId: cartId,
      productId: product.value.id,
      quantity: selectedQuantity.value
    };
    await proxy.$api.getCartById(cartId);
    await proxy.$api.addProductToCart(cartItemDto);
    store.state.currentCartId = cartId;

    ElMessage({
      message: 'Product added to cart!',
      type: 'success',
      duration: 1000
    });
  } catch (error) {
    console.error('Error adding product to cart:', error);
  }
};

onMounted(() => {
  getProductById();
});
</script>

<style scoped>
.close-button {
    cursor: pointer
}

.product-view {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  position: relative;
}

.divider {
  height: 1px;
  background-color: #ebeef5;
  margin: 20px 0;
}

.product-name {
  font-size: 24px;
  margin-bottom: 30px;
}

.product-image {
  width: 300px;
  height: 300px;
  object-fit: contain;
  display: block;
  margin: 20px auto;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.no-image {
  width: 300px;
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 100px;
  color: #ccc;
  margin: 20px auto;
  border: 2px dashed #ddd;
  border-radius: 8px;
}

.cart-controls {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 20px;
  gap: 12px;
}

.quantity-input {
  width: 120px;
}

.add-to-cart-button {
  white-space: nowrap;
}

.discount-tags {
  display: flex;
  justify-content: flex-end;
  flex-wrap: wrap;
  margin: 12px 0;
}

.tabs-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.close-button {
  display: flex;
  justify-content: right;
  margin-bottom: 20px;
}
.close-button .el-icon {
  font-size: 25px;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.close-button .el-icon:hover {
  transform: scale(1.1);
}

.product-details-descriptions :deep(.el-descriptions__cell) {
  width: 50%;
}
</style>