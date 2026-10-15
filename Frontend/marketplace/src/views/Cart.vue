<template>
  <div class="checkout-page">
    <div class="close-button-wrapper">
      <div class="close-button">
        <el-icon @click="handleCloseClick">
          <Close />
        </el-icon>
      </div>
    </div>
    <h2 class="checkout-title">Cart</h2>
    <el-divider />

    <div v-for="(group, sellerId) in groupedCartItems" :key="sellerId" class="seller-section">
      <div class="seller-header">
        <el-avatar icon="UserFilled" />
        <span class="seller-text">Seller {{ sellerId }}</span>

        <el-select
          v-model="store.state.cartDeliveryOptions[sellerId]"
          placeholder="Select Delivery"
          size="small"
          class="delivery-select"
          @change="updateDeliveryFee"
        >
          <el-option label="Standard" value="standard" />
          <el-option label="Express" value="express" />
        </el-select>

      </div>

      <div
        class="cart-item"
        v-for="item in group"
        :key="item.id"
      >
        <el-image class="product-img" :src="getImageUrl(item.productImage)" fit="cover" />
        <div class="product-info">
          <div class="product-name">{{ item.productName }}</div>
          <div class="product-price">RM{{ item.unitPrice.toFixed(2) }}</div>
        </div>
        <div class="product-actions">
          <div class="co2">{{ item.co2 }} kg CO2</div>
          <el-icon @click="deleteCartItem(item.id, item.cartId)" style="cursor: pointer; color: red;">
            <Delete />
          </el-icon>
          <div class="quantity-control">
            <el-button size="small" @click="changeQuantity(item.id, item.quantity - 1)" :disabled="item.quantity <= 1">-</el-button>
            <span>{{ item.quantity }}</span>
            <el-button size="small" @click="changeQuantity(item.id, item.quantity + 1)">+</el-button>
          </div>
        </div>
      </div>
      <el-divider />
    </div>

    <div class="checkout-footer">
      <div class="subtotal">
        Subtotal: RM{{ subtotal.toFixed(2) }}<br />
        Delivery Fee: RM{{ deliveryFee.toFixed(2) }}<br />
        <strong>Total: RM{{ totalWithDelivery.toFixed(2) }}</strong>
        <span class="co2-total">({{ totalCO2 }} kg CO2)</span>
      </div>
      <div>
        <el-button type="danger" @click="deleteAllCartItem(cart.id)">Delete All</el-button>
        <el-button type="success" @click="checkout">Checkout</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAllDataStore } from '@/stores';
import { ref, computed, getCurrentInstance, onMounted } from 'vue'

import { useRouter } from 'vue-router'

const router = useRouter()

const {proxy} = getCurrentInstance()

const store = useAllDataStore();

const getImageUrl = (img) => {
  return new URL(`../assets/${img}`, import.meta.url).href
}

const cart = ref({
  id: null,
  customerId: null,
  cartItems: [
    {
      id: null,
      cartId:null,
      productName: '',
      quantity: 0,
      productImage: '',
      co2: 0,
      unitPrice: 0,
      subtotal: 0,
      sellerId: null,
      sellerImage: '',
    }
  ]
});

const getCartById = async () => {
  try{
    const response = await proxy.$api.getCartById(currentCartId.value);
    console.log(response.data.data)
    cart.value = response.data.data;

    if (cart.value && Array.isArray(cart.value.cartItems)) {
        const sellerIds = [...new Set(cart.value.cartItems.map(item => item.sellerId))];
        store.initializeDeliveryOptions(sellerIds);
    }

    await updateDeliveryFee();
  } catch (error) {
    console.error('Error retrieving product:', error);
  }
}

const groupedCartItems = computed(() => {
  const groups = {}
  if (!cart.value || !Array.isArray(cart.value.cartItems)) {
    return groups
  }

  for (const item of cart.value.cartItems) {
    if (!groups[item.sellerId]) {
      groups[item.sellerId] = []
    }
    groups[item.sellerId].push(item)
  }
  return groups
})

const deliveryFee = ref(0);

const updateDeliveryFee = async () => {
  const sellerIds = Object.keys(store.state.cartDeliveryOptions);
  const deliveries = sellerIds.map(id => store.state.cartDeliveryOptions[id]);

  try {
    const response = await proxy.$api.getDeliveryFee(currentCartId.value, deliveries);
    deliveryFee.value = response.data.data;
  } catch (error) {
    console.error("Failed to get delivery fee:", error);
    deliveryFee.value = 0;
  }
};

const totalWithDelivery = computed(() => {
  return subtotal.value + deliveryFee.value;
});

const subtotal = computed(() =>
  Array.isArray(cart.value?.cartItems)
    ? cart.value.cartItems.reduce((sum, item) => sum + item.subtotal, 0)
    : 0
)

const totalCO2 = computed(() =>
  Array.isArray(cart.value?.cartItems)
    ? cart.value.cartItems.reduce((sum, item) => sum + item.co2 * item.quantity, 0)
    : 0
)

const changeQuantity = async (cartItemId, newQuantity) => {
  if (newQuantity < 1) return;

  try {
    const response = await proxy.$api.updateCartItemQuantity(newQuantity, cartItemId);
    cart.value = response.data.data;

    if (cart.value && Array.isArray(cart.value.cartItems)) {
      const sellerIds = [...new Set(cart.value.cartItems.map(item => item.sellerId))];
      store.initializeDeliveryOptions(sellerIds);
    }

    await updateDeliveryFee();
  } catch (error) {
    console.error('Failed to update quantity:', error);
  }
};

const currentCartId = computed(() => store.state.currentCartId);

const handleCloseClick = () => {
    router.back()
}

const deleteCartItem = async (cartItemId, cartId) => {
  try {
    await proxy.$api.deleteCartItemById(cartItemId, cartId);
    await getCartById();
  } catch (error) {
    console.error('Failed to delete cart item:', error);
  }
}

const deleteAllCartItem = async (cartId) => {
  try {
    await proxy.$api.deleteAllCartItems(cartId);
    await getCartById();
  } catch (error) {
    console.error('Failed to delete all cart item:', error);
  }
}

const checkout = () => {
  router.push('/checkout')
}

onMounted(() => {
  getCartById();
});
</script>

<style scoped>
.checkout-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.checkout-title {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
}

.close-button-wrapper {
  position: relative;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
  z-index: 10;
}

.address-section,
.payment-section,
.voucher-section {
  margin-bottom: 30px;
}

.seller-section {
  margin-bottom: 30px;
}

.seller-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.seller-text {
  font-weight: bold;
  margin-left: 10px;
  flex: 1;
}

.delivery-select {
  width: 140px;
}

.cart-item {
  display: flex;
  align-items: center;
  border-bottom: 1px solid #ebeef5;
  padding: 10px 0;
}

.product-img {
  width: 60px;
  height: 60px;
  margin-right: 20px;
}

.product-info {
  flex: 1;
}

.product-name {
  font-weight: bold;
}

.product-price {
  color: #606266;
}

.product-actions {
  display: flex;
  align-items: center;
  flex-direction: column;
  gap: 10px;
}

.co2 {
  color: green;
}

.checkout-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  gap: 20px;
  flex-wrap: wrap;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 5px;
}

.subtotal {
  font-weight: bold;
}

.co2-total {
  color: green;
  margin-left: 10px;
}
</style>
