<template>
  <div class="success-order-page">
    <h2 class="title">Order Successful!</h2>
    <p>Your orders have been placed successfully. Below are the details:</p>

    <div
      v-for="(order, index) in successOrder"
      :key="order.id"
      class="order-card"
    >
      <el-card>
        <h3>Order #{{ order.id }} ({{ order.orderStatus }})</h3>

        <div class="section">
          <strong>CO₂ Reduction:</strong>
          <span>{{ order.co2 ? 'Yes' : 'No' }}</span>
        </div>

        <div class="section">
          <strong>Delivery:</strong>
          <p>
            Method: {{ order.delivery.method }} ({{ order.delivery.courier }})<br />
            Fee: RM{{ order.delivery.price ? order.delivery.price.toFixed(2) : '0.00' }}
            Emission Rate: {{ order.delivery.emissionRate }} kg CO₂<br />
            Address: {{ formatAddress(order.delivery.shippingAddress) }}<br />
            Dispatched At: {{ order.delivery.dispatchedAt || '-' }}<br />
            Delivered At: {{ order.delivery.deliveredAt || '-' }}
          </p>
        </div>

        <div class="section">
          <strong>Payment:</strong>
          <p>
            Method: {{ order.payment.method }}<br />
            Amount Paid: RM{{ order.payment.amount.toFixed(2) }}<br />
            Date: {{ order.payment.paymentDate }}
          </p>
        </div>

        <div class="section" v-if="order.voucher">
          <strong>Voucher Applied:</strong>
          <p>
            {{ order.voucher.name }} - RM{{ order.voucher.amount.toFixed(2) }} off<br />
            Category: {{ order.voucher.category }}<br />
            Valid until: {{ order.voucher.expiryDate }}
          </p>
        </div>

        <div class="section">
          <strong>Items:</strong>
          <ul>
            <li v-for="item in order.orderItems" :key="item.id">
              {{ item.productName }} x{{ item.quantity }} (RM{{ item.unitPrice.toFixed(2) }})
            </li>
          </ul>
        </div>
      </el-card>
    </div>
    <el-button @click="backToHome">Back to Homepage</el-button>
  </div>
</template>

<script setup>
import { useAllDataStore } from '@/stores'
import {onMounted} from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const store = useAllDataStore()
const successOrder = store.state.successOrder

const formatAddress = (address) => {
  return `${address.street}, ${address.city}, ${address.state}, ${address.postalCode} (${address.additionalInfo})`
}

const checkOrder = () => {
    console.log(successOrder.value)
}

const backToHome = () => {
  router.push('/main');
}

onMounted(() => {
    checkOrder()
})
</script>

<style scoped>
.success-order-page {
  max-width: 800px;
  margin: 30px auto;
  padding: 20px;
}
.title {
  text-align: center;
  font-size: 28px;
  margin-bottom: 20px;
}
.order-card {
  margin-bottom: 30px;
}
.section {
  margin-top: 15px;
}
</style>
