<template>
  <div class="checkout-page">
    <div class="close-button-wrapper">
      <div class="close-button">
        <el-icon @click="handleCloseClick">
          <Close />
        </el-icon>
      </div>
    </div>
    <h2 class="checkout-title">Checkout</h2>
    <el-divider />

    <div class="address-section">
      <h3>Delivery Address</h3>
      <p>{{ address }}</p>
    </div>
    <el-divider/>
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
          <span>x{{ item.quantity }}</span>
        </div>
      </div>
      <div class="voucher-section">
        <h3>Voucher</h3>
        <el-button @click="() => openVoucherDialog(sellerId)">Apply Voucher</el-button>
        <p>Selected: {{ selectedVouchers[sellerId]?.name || 'None' }}</p>

        <el-dialog
          v-model="voucherDialogVisible[sellerId]"
          title="Available Vouchers"
        >
          <el-radio-group
            v-model="selectedVouchers[sellerId]"
            class="vertical-radio-group"
            @change="() => (voucherDialogVisible[sellerId] = false)"
          >
            <el-radio
              v-for="voucher in applicableVouchersPerSeller[sellerId]"
              :key="voucher.id"
              :label="voucher"
            >
              {{ voucher.name }} - RM{{ voucher.amount }}
            </el-radio>
          </el-radio-group>

          <template #footer>
            <el-button
              type="danger"
              @click="clearVoucher(sellerId)"
            >Clear voucher</el-button>
          </template>
        </el-dialog>
      </div>

      <div class="footprint-switch">
        <span>Reduce Carbon Footprint (+ RM0.10)</span>
        <el-switch v-model="reduceFootprints[sellerId]" size="small" />
      </div>

      <div class="subtotal">
        Section Total: RM{{ sectionTotals[sellerId]?.toFixed(2) || '0.00' }}
      </div>

      <el-divider />
    </div>

    <div class="payment-section">
      <h3>Payment Method</h3>
      <el-button @click="paymentDialogVisible = true">Select Payment Method</el-button>
      <p>Selected: {{ selectedPayment }}</p>
    </div>
    <el-dialog v-model="paymentDialogVisible" title="Choose Payment Method">
        <el-radio-group
          v-model="selectedPayment"
          class="vertical-radio-group"
          @change="onPaymentMethodChange"
        >
            <el-radio label="Cash on Delivery">Cash on Delivery</el-radio>
            <el-radio label="Card">Card</el-radio>
            <el-radio label="Bank Transfer">Bank Transfer</el-radio>
            <el-radio label="EWallet">EWallet</el-radio>
        </el-radio-group>
    </el-dialog>

    <div class="checkout-footer">
      <div class="subtotal">
        <strong>Final Total: RM{{ finalTotal.toFixed(2) }}</strong>
      </div>
      <el-button type="success" @click="handlePay">Pay</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, getCurrentInstance, onMounted } from 'vue'
import { useAllDataStore } from '@/stores';
import { useRouter } from 'vue-router'

const router = useRouter()

const {proxy} = getCurrentInstance()

const store = useAllDataStore();


const getImageUrl = (img) => {
  return new URL(`../assets/${img}`, import.meta.url).href
}

const address = ref("No. 12, Jalan Hijau, 12345 Eco City, Malaysia")

const cart = ref({
  id: null,
  customerId: null,
  cartItems: [
    {
      id: null,
      cartId:null,
      productName: '',
      productCategory: '',
      quantity: 0,
      productImage: '',
      co2: 0,
      unitPrice: 0,
      subtotal: 0,
      sellerId: null,
      sellerImage: ''
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

const updateDeliveryFee = async () => {
  const sellerIds = Object.keys(store.state.cartDeliveryOptions);
};

const currentCartId = computed(() => store.state.currentCartId);

const paymentDialogVisible = ref(false)
const selectedPayment = ref('Cash on Delivery')

const vouchers = ref([
  {
    id: null,
    name: '',
    description: '',
    startDate: '',
    expiryDate: '',
    amount: 0,
    category: '',
    quantity: 0
  },
]);

const getVouchers = async () => {
  try {
    const response = await proxy.$api.getAllVouchers();
    vouchers.value = response.data.data;
  } catch (error) {
    console.error("Failed to fetch vouchers:", error);
  }
};

const selectedVouchers = ref({});
const reduceFootprints = ref({});
const voucherDialogVisible = ref({});

const usedVoucherIds = computed(() => {
  return Object.values(selectedVouchers.value)
    .filter(v => v !== null)
    .map(v => v.id);
});


const applicableVouchersPerSeller = computed(() => {
  const result = {};
  for (const sellerId in groupedCartItems.value) {
    const items = groupedCartItems.value[sellerId];
    const categories = [...new Set(items.map(i => i.productCategory))];
    result[sellerId] = vouchers.value.filter(v =>
      categories.includes(v.category) &&
      (
        !usedVoucherIds.value.includes(v.id) ||
        selectedVouchers.value[sellerId]?.id === v.id
      )
    );
  }
  return result;
});

const sectionTotals = computed(() => {
  const result = {};
  for (const sellerId in groupedCartItems.value) {
    const items = groupedCartItems.value[sellerId];
    let subtotal = items.reduce((sum, item) => sum + item.subtotal, 0);
    const voucher = selectedVouchers.value[sellerId];
    const footprintFee = reduceFootprints.value[sellerId] ? 0.10 : 0;
    const deliveryFee = getDeliveryFeePerSeller(sellerId);

    if (voucher) subtotal -= voucher.amount;
    subtotal += footprintFee + deliveryFee;
    result[sellerId] = subtotal;
  }
  return result;
});

const finalTotal = computed(() =>
  Object.values(sectionTotals.value).reduce((sum, val) => sum + val, 0)
);

const openVoucherDialog = (sellerId) => {
  voucherDialogVisible.value[sellerId] = true;
};

const clearVoucher = (sellerId) => {
  selectedVouchers.value[sellerId] = null;
  voucherDialogVisible.value[sellerId] = false;
};

const onPaymentMethodChange = () => {
  paymentDialogVisible.value = false;
};

const getDeliveryFeePerSeller = (sellerId) => {
  const option = store.state.cartDeliveryOptions[sellerId];
  if (option === 'express') return 3;
  if (option === 'standard') return 1;
  return 0;
};

const successOrder = ref([])

const handlePay = async () => {
  const ordersToSend = [];

  for (const sellerId in groupedCartItems.value) {
    const items = groupedCartItems.value[sellerId];

    const deliveryMethod = store.state.cartDeliveryOptions[sellerId];
    const deliveryEnum = deliveryMethod === 'express' ? 'Express' : 'Standard';

    const delivery = {
      method: deliveryEnum,
      courier: 'Eco Courier',
      shippingAddress: {
        street: 'No. 12, Jalan Hijau',
        city: 'Eco City',
        state: 'Selangor',
        postalCode: '12345',
        additionalInfo: 'Unit A'
      }
    };

    const payment = {
      method: mapPaymentMethod(selectedPayment.value),
      amount: sectionTotals.value[sellerId],
      paymentDate: new Date().toISOString().split('T')[0]
    };

    const cartItemIds = items.map(item => item.id);
    const voucher = selectedVouchers.value[sellerId];

    const orderDto = {
      co2: !!reduceFootprints.value[sellerId],
      delivery,
      payment,
      voucherId: voucher ? voucher.id : null,
      customerId: cart.value.customerId,
      cartItemIds
    };

    ordersToSend.push(orderDto);
  }

  try {
    for (const order of ordersToSend) {
      const response = await proxy.$api.addOrder(order);
      successOrder.value.push(response.data.data);
    }
    const sellerIds = Object.keys(store.state.cartDeliveryOptions);
    for (const sellerId of sellerIds) {
      store.state.cartDeliveryOptions[sellerId] = 'standard';
    }
    store.state.successOrder = successOrder.value
    console.log(store.state.successOrder)
    router.push('/successOrder');
  } catch (error) {
    console.error('Failed to place one or more orders:', error);
  }
};

const mapPaymentMethod = (label) => {
  switch (label) {
    case 'Cash on Delivery': return 'COD';
    case 'Card': return 'Card';
    case 'Bank Transfer': return 'Bank';
    case 'EWallet': return 'EWallet';
    default: return 'COD';
  }
};

const handleCloseClick = () => {
    router.back()
}

onMounted(() => {
  getCartById();
  getVouchers();
});

</script>

<style scoped>
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

.subtotal {
  font-weight: bold;
}

.co2-total {
  color: green;
  margin-left: 10px;
}

.footprint-switch {
  display: flex;
  align-items: center;
  gap: 10px;
}

.vertical-radio-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: flex-start;
}
</style>
