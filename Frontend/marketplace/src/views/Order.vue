<template>
  <div class="all-orders-container">
    <el-card class="all-orders-card">
      <template #header>
        <div class="card-header">
          <span>All Orders</span>
          <el-icon @click="goBack" style="cursor: pointer;"><ArrowLeft /></el-icon>
        </div>
      </template>

      <div class="orders-list">
        <div v-if="allOrders.length === 0" class="no-orders">
          No orders found.
        </div>
        <div v-for="order in allOrders" :key="order.id" class="order-item-full">
          <el-row class="order-header">
            <el-col :span="12">
              <div class="order-id">Order ID: {{ order.id }}</div>
            </el-col>
            <el-col :span="12" style="text-align: right;">
              <div class="order-status">Status: {{ order.orderStatus }}</div>
            </el-col>
          </el-row>
          <el-row class="order-details-row">
            <el-col :span="24">
              <div v-for="item in order.orderItems" :key="item.id" class="product-line-item">
                <div class="product-thumbnail">
                  <img
                    v-if="item.productImage"
                    :src="getImageUrl(item.productImage)"
                    alt="Product"
                    style="width: 100%; height: 100%; object-fit: cover; border-radius: 4px;"
                  />
                  <el-icon v-else style="font-size: 40px; color: #dcdfe6;"><Picture /></el-icon>
                </div>
                <div class="product-info-details">
                  <span class="product-name">{{ item.productName }}</span>
                  <span class="product-quantity">x{{ item.quantity }}</span>
                  <span class="product-unit-price">RM{{ item.unitPrice.toFixed(2) }}</span>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row class="order-footer">
            <el-col :span="12">
              <div class="payment-date">Paid on: {{ order.payment.paymentDate }}</div>
            </el-col>
            <el-col :span="12" style="text-align: right;">
              <div class="total-amount">Total: RM{{ order.payment.amount }}</div>
            </el-col>
          </el-row>
          <el-divider v-if="order.id !== allOrders[allOrders.length - 1].id" />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { useAllDataStore } from '@/stores'
import { ref, onMounted, getCurrentInstance, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowLeft, Picture } from '@element-plus/icons-vue';
const store = useAllDataStore()
const {proxy} = getCurrentInstance()
const router = useRouter();
const getImageUrl = (user)=>{
    return new URL(`../assets/${user}`,import.meta.url).href
}
const allOrders = ref([{
  orderStatus: '',
  co2: null,
  delivery: {
    id: null,
    method: '',
    courier: '',
    dispatchedAt: '',
    deliveredAt: '',
    shippingAddress: {
      street: '',
      city: '',
      state: '',
      postalCode: '',
      additionalInfo: ''
    }
  },
  payment: {
    id: null,
    method: '',
    amount: 0,
    paymentDate: ''
  },
  voucher: {
    id: null,
    name: '',
    description: '',
    amount: 0,
    category: ''
  },
  customerId: null,
  orderItems: [{
    id: null,
    orderId: null,
    productId: null,
    productName: '',
    productImage: '',
    quantity: 0,
    unitPrice: 0,
    subtotal: 0,
    sellerId: null
  }]
}]);

const customerId = computed(()=>store.state.userId);
const getAllOrdersByCustomer = async () => {
  try{
    const response = await proxy.$api.getAllOrdersByCustomer(customerId.value);
     allOrders.value = response.data.data.sort((a, b) => {
      return new Date(b.payment.paymentDate) - new Date(a.payment.paymentDate);
    });
  } catch (error) {
    console.error('Error fetching orders:', error);
  }
}

onMounted(async () => {
  getAllOrdersByCustomer();
});

const goBack = () => {
  router.back();
};
</script>

<style scoped>
.all-orders-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  background-color: #f5f7fa;
}

.all-orders-card {
  width: 100%;
  max-width: 800px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
}

.orders-list {
  padding: 15px;
}

.no-orders {
  text-align: center;
  color: #909399;
  padding: 20px;
}

.order-item-full {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  margin-bottom: 20px;
  padding: 15px;
  background-color: #fff;
}

.order-header {
  margin-bottom: 10px;
  font-size: 16px;
  font-weight: bold;
}

.order-details-row {
  margin-bottom: 10px;
}

.product-line-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.product-thumbnail {
  width: 50px;
  height: 50px;
  background-color: #f0f2f5;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 15px;
  border-radius: 4px;
  flex-shrink: 0;
}

.product-info-details {
  flex-grow: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-name {
  font-weight: bold;
  font-size: 14px;
}

.product-quantity {
  font-size: 13px;
  color: #606266;
}

.product-unit-price {
  font-size: 13px;
  color: #606266;
}

.order-footer {
  margin-top: 10px;
  font-size: 15px;
  font-weight: bold;
}

.payment-date {
  color: #909399;
  font-weight: normal;
  font-size: 14px;
}

.el-divider--horizontal {
  margin: 20px 0;
}
</style>