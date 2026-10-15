<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>Profile</span>
          <el-icon @click="closeProfile" style="cursor: pointer; margin-left: 10px;"><Close /></el-icon>
        </div>
      </template>

      <div style="text-align: right; margin-bottom: 10px;">
        <el-button type="danger" size="small" @click="logout">Logout</el-button>
      </div>

      <div class="user-info">
        <div class="avatar-placeholder">
          <el-image :src="getImageUrl(userImage)" fit="contain">
            <template #error>
              <div class="image-slot">
                <el-icon style="font-size: 48px; color: #909399;"><Picture /></el-icon>
              </div>
            </template>
          </el-image>

          <el-icon class="edit-avatar-icon" @click="showEditAvatar = true">
            <EditPen />
          </el-icon>
        </div>
        <div class="details">
          <div class="info-item">
            <span class="label">Username :</span>
            <span class="value">{{ userRole === 'SELLER' ? seller.username : customer.username }}</span>
          </div>
          <div class="info-item">
            <span class="label">Email :</span>
            <span class="value">{{ userRole === 'SELLER' ? seller.email : customer.email }}</span>
          </div>
          <div class="info-item">
            <span class="label">Phone :</span>
            <span class="value">{{ userRole === 'SELLER' ? seller.phone : customer.phone }}</span>
          </div>
          <div v-if="userRole === 'SELLER'" class="info-item">
            <span class="label">Company :</span>
            <span class="value">{{ seller.company }}</span>
          </div>
        </div>
        <div class="profile-actions">
          <el-button type="success" @click="editProfile">Edit</el-button>
          <el-button type="warning" style="margin-top: 10px;" @click="changePassword">Change Password</el-button>
        </div>
      </div>

      <el-divider />

      <div class="address-section">
        <div class="section-header">
          <h3>Address</h3>
        </div>
        <div class="address-box">
          <el-input
            type="textarea"
            :rows="4"
            placeholder="Address Details"
            :model-value="formattedAddress"
            readonly
          ></el-input>
          <el-icon class="edit-address-icon" @click="editAddress"><EditPen /></el-icon>
        </div>
      </div>

      <el-dialog v-model="showEditAvatar" title="Update Profile Picture" width="400px">
        <div style="margin-bottom: 10px;">
          <el-image
            v-if="customer.image"
            :src="getImageUrl(customer.image)"
            style="width: 100px; height: 100px;"
            fit="cover"
          />
          <div v-else style="width: 100px; height: 100px; background-color: #f0f0f0; display: flex; align-items: center; justify-content: center;">
            <el-icon><Picture /></el-icon>
          </div>
        </div>
        <input type="file" @change="handleAvatarChange" />

        <template #footer>
          <el-button @click="showEditAvatar = false">Cancel</el-button>
          <el-button type="primary" @click="submitAvatarChange">Submit</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="showEditProfile" title="Edit Profile" width="400px">
        <el-form label-width="100px">
          <el-form-item label="Email">
            <el-input v-model="editEmail" />
          </el-form-item>
          <el-form-item label="Phone">
            <el-input v-model="editPhone" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showEditProfile = false">Cancel</el-button>
          <el-button type="primary" @click="submitEditProfile">Save</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="showChangePassword" title="Change Password" width="400px">
        <el-form label-width="120px">
          <el-form-item label="New Password">
            <el-input v-model="newPassword" type="password" />
          </el-form-item>
          <el-form-item label="Confirm Password">
            <el-input v-model="confirmPassword" type="password" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showChangePassword = false">Cancel</el-button>
          <el-button type="primary" @click="submitChangePassword">Change</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="showEditAddress" title="Edit Address" width="500px">
        <el-form label-width="120px">
          <el-form-item label="Street">
            <el-input v-model="currentAddress.street" />
          </el-form-item>
          <el-form-item label="City">
            <el-input v-model="currentAddress.city" />
          </el-form-item>
          <el-form-item label="State">
            <el-input v-model="currentAddress.state" />
          </el-form-item>
          <el-form-item label="Postal Code">
            <el-input v-model="currentAddress.postalCode" />
          </el-form-item>
          <el-form-item label="Additional Info">
            <el-input v-model="currentAddress.additionalInfo" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showEditAddress = false">Cancel</el-button>
          <el-button type="primary" @click="submitEditAddress">Save</el-button>
        </template>
      </el-dialog>


      <el-divider />

      <div class="recent-orders-section" v-if="userRole === 'CUSTOMER'">
        <div class="section-header">
          <h3>Recent Orders (Within 30 Days)</h3>
          <el-button type="text" @click="viewAllOrders">View All Orders</el-button>
        </div>
        <div class="orders-list">
          <div v-if="recentOrders.length === 0" class="no-orders">
            No recent orders.
          </div>
          <div v-for="order in recentOrders" :key="order.id" class="order-item">
            <div class="order-details">
              <div class="product-thumbnail">
                <el-image v-if="order.orderItems.length > 0" :src="getImageUrl(order.orderItems[0].productImage)" fit="contain">
                    <template #error>
                        <div class="image-slot">
                            <el-icon style="font-size: 40px; color: #dcdfe6;"><Picture /></el-icon>
                        </div>
                    </template>
                </el-image>
                <el-icon v-else style="font-size: 40px; color: #dcdfe6;"><Picture /></el-icon>
            </div>
              <div class="product-info">
                <div v-for="item in order.orderItems" :key="item.id" class="order-item-detail">
                  <span class="product-name">{{ item.productName }}</span>
                  <span class="product-quantity">x{{ item.quantity }}</span>
                </div>
                <div class="product-price">RM{{ order.payment.amount.toFixed(2) }}</div>
              </div>
              <div class="order-actions">
                <el-button type="danger" size="small" class="report-button" @click="openDialog('report', order)">Report</el-button>
                <el-button type="success" size="small" class="rate-button" @click="openDialog('rate', order)">Rate</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
  <el-dialog
    v-model="showDialog"
    :title="dialogMode === 'report' ? 'Report Product' : 'Rate Product'"
    width="400px"
    @close="closeDialog"
  >
    <div v-if="selectedOrder">
      <el-form label-width="100px">
        <el-form-item label="Product">
          <el-select v-model="selectedProductId" placeholder="Select product">
            <el-option
              v-for="item in selectedOrder.orderItems"
              :key="item.id"
              :label="item.productName"
              :value="dialogMode === 'rate' ? item.productId : item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item v-if="dialogMode === 'rate'" label="Rating">
          <el-rate v-model="rating" :max="5" show-score />
        </el-form-item>

        <el-form-item label="Comment">
          <el-input v-model="comment" type="textarea" rows="3" placeholder="Enter your message..." />
        </el-form-item>
      </el-form>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="closeDialog">Cancel</el-button>
        <el-button type="primary" @click="submitDialog">Submit</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted, computed, getCurrentInstance } from 'vue';
import { useRouter } from 'vue-router';
import { Close, EditPen, Picture } from '@element-plus/icons-vue';
import { useAllDataStore } from '@/stores';

const store = useAllDataStore();

const {proxy} = getCurrentInstance()

const router = useRouter();

const getImageUrl = (user)=>{
    return new URL(`../assets/${user}`,import.meta.url).href
}

const customerId = computed(()=>store.state.userId);

const userRole = computed(()=>store.state.userRole);

const customer = ref({
  username: '',
  email: '',
  phone: '',
  image: '',
  address: {
    street: '',
    city: '',
    state: '',
    postalCode: '',
    additionalInfo: ''
  }
});

const seller = ref({
  username: '',
  email: '',
  phone: '',
  image: '',
  address: {
    street: '',
    city: '',
    state: '',
    postalCode: '',
    additionalInfo: ''
  },
  company: ''
});

const getCustomerById = async () => {
  try{
    const response = await proxy.$api.getCustomerById(customerId.value);
    customer.value = response.data.data;
  } catch (error) {
    console.error('Error fetching customer:', error);
  }
}

const getSellerById = async () => {
  try{
    const response = await proxy.$api.getSellerById(customerId.value);
    seller.value = response.data.data;
  } catch (error) {
    console.error('Error fetching seller:', error);
  }
}

const getAdminById = async () => {
  try{
    const response = await proxy.$api.getAdminById(customerId.value);
    customer.value = response.data.data;
  } catch (error) {
    console.error('Error fetching admin:', error);
  }
}

const formattedAddress = computed(() => {
  const addr =
    userRole.value === 'SELLER'
      ? seller.value.address || {}
      : customer.value.address || {};

  return `${addr.street || ''}, ${addr.city || ''}, ${addr.state || ''}, ${addr.postalCode || ''}, ${addr.additionalInfo || ''}`.trim();
});

const currentAddress = computed(() => {
  return userRole.value === 'SELLER' ? seller.value.address : customer.value.address;
});

const userImage = computed(() => {
  if (userRole.value === 'SELLER') {
    return seller.value.image;
  }
  return customer.value.image;
});

const showEditAvatar = ref(false);

const avatarFile = ref(null);

const handleAvatarChange = (event) => {
  avatarFile.value = event.target.files[0];
};

const submitAvatarChange = async () => {
  console.log(userRole.value)
  await proxy.$api.uploadProfilePicture(avatarFile.value, customerId.value);

  console.log(userRole.value)
  if (userRole.value === 'ADMIN') {
    await getAdminById();
    console.log(customer.value);
  } else if (userRole.value === 'SELLER') {
    await getSellerById();
    console.log(seller.value);
  } else if (userRole.value === 'CUSTOMER') {
    await getCustomerById();
  }
  showEditAvatar.value = false;
  avatarFile.value = null;
};

const showEditProfile = ref(false);
const showChangePassword = ref(false);
const showEditAddress = ref(false);

const editEmail = ref('');
const editPhone = ref('');
const newPassword = ref('');
const confirmPassword = ref('');

const editProfile = () => {
  editEmail.value = customer.value.email;
  editPhone.value = customer.value.phone;
  showEditProfile.value = true;
};

const submitEditProfile = async () => {
  try {
    const profile = {
      email: editEmail.value,
      phone: editPhone.value
    };
    await proxy.$api.updateProfile(customerId.value, profile);
    switch (userRole.value) {
    case 'ADMIN':
      getAdminById();
      break;
    case 'SELLER':
      getSellerById();
      break;
    case 'CUSTOMER':
      getCustomerById();
      getAllOrdersByCustomer();
      break;
  }
  showEditProfile.value = false;
  } catch (error) {
    console.error('Failed to update profile:', error);
  }
};

const changePassword = () => {
  newPassword.value = '';
  confirmPassword.value = '';
  showChangePassword.value = true;
};

const submitChangePassword = async () => {
  try {
    const password = {
      newPassword: newPassword.value,
      confirmPassword: confirmPassword.value
    };
    await proxy.$api.updatePassword(customerId.value, password);
    showChangePassword.value = false;
  } catch (error) {
    console.error('Failed to change password:', error);
  }
};

const editAddress = () => {
  if (userRole.value === 'SELLER' && !seller.value.address) {
    seller.value.address = {
      street: '',
      city: '',
      state: '',
      postalCode: '',
      additionalInfo: ''
    };
  } else if (!customer.value.address) {
    customer.value.address = {
      street: '',
      city: '',
      state: '',
      postalCode: '',
      additionalInfo: ''
    };
  }
  showEditAddress.value = true;
};

const submitEditAddress = async () => {
  try {
    if (userRole.value === 'SELLER') {
      await proxy.$api.updateAddress(customerId.value, seller.value.address);
      getSellerById();
    } else if(userRole.value === 'CUSTOMER'){
      await proxy.$api.updateAddress(customerId.value, customer.value.address);
      getCustomerById();
    } else {
      await proxy.$api.updateAddress(customerId.value, customer.value.address);
      getAdminById();
    }
    showEditAddress.value = false;
  } catch (error) {
    console.error('Failed to update address:', error);
  }
};


const logout = () => {
  store.state.userId = null;
  store.state.userRole = '';
  router.push('/login');
};

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

const recentOrders = computed(() => {
  const today = new Date();
  return allOrders.value.filter(order => {
    const orderDate = new Date(order.payment.paymentDate);
    const diffInTime = today.getTime() - orderDate.getTime();
    const diffInDays = diffInTime / (1000 * 3600 * 24);
    return diffInDays <= 30;
  });
});

const getAllOrdersByCustomer = async () => {
  try{
    const response = await proxy.$api.getAllOrdersByCustomer(customerId.value);
    allOrders.value = response.data.data;
  } catch (error) {
    console.error('Error fetching orders:', error);
  }
}

const showDialog = ref(false);
const dialogMode = ref('');
const selectedOrder = ref(null);
const selectedProductId = ref(null);
const rating = ref(0);
const comment = ref('');

const openDialog = (mode, order) => {
  dialogMode.value = mode;
  selectedOrder.value = order;
  selectedProductId.value = order.orderItems.length > 0
  ? (mode === 'rate' ? order.orderItems[0].productId : order.orderItems[0].id)
  : null;
  rating.value = 0;
  comment.value = '';
  showDialog.value = true;
};

const closeDialog = () => {
  showDialog.value = false;
};

const submitDialog = async () => {
  const orderId = selectedOrder.value?.id;

  if (!selectedProductId.value || !orderId) return;

  if (dialogMode.value === 'rate') {
    const review = {
      comment: comment.value,
      rating: rating.value,
      reviewDate: new Date().toISOString().split('T')[0],
      product_id: selectedProductId.value,
      customer_id: customerId.value,
    };
    await proxy.$api.addReview(review);
  } else if (dialogMode.value === 'report') {
    const orderItem = selectedOrder.value.orderItems.find(
      (item) => item.id === selectedProductId.value
    );
    const complaint = {
      issue: comment.value,
      orderItemId: selectedProductId.value,
      customerId: customerId.value,
      sellerId: orderItem.sellerId
    };
    await proxy.$api.addComplaint(complaint);
  }

  closeDialog();
};

onMounted( () => {
  switch (userRole.value) {
    case 'ADMIN':
      getAdminById();
      break;
    case 'SELLER':
      getSellerById();
      break;
    case 'CUSTOMER':
      getCustomerById();
      getAllOrdersByCustomer();
      break;
  }
});

const closeProfile = () => {
  router.back();
};

const viewAllOrders = () => {
  router.push('/order');
};
</script>

<style scoped>
.edit-avatar-icon {
  position: absolute;
  top: 15px;
  right: 1px;
  font-size: 20px;
  color: #409EFF;
  background: white;
  border-radius: 50%;
  padding: 3px;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.2);
  cursor: pointer;
}

.profile-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  background-color: #f5f7fa;
}

.profile-card {
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

.user-info {
  display: flex;
  align-items: center;
  padding: 20px 0;
  position: relative;
}

.avatar-placeholder {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background-color: #f0f2f5;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 20px;
  border: 1px solid #dcdfe6;
  overflow: hidden;
}

.details {
  flex-grow: 1;
  text-align: left;
}

.info-item {
  margin-bottom: 8px;
  font-size: 16px;
}

.info-item .label {
  font-weight: bold;
  display: inline-block;
  width: 90px;
}

.info-item .value {
  margin-left: 10px;
}

.edit-button {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  padding: 8px 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.section-header h3 {
  margin: 0;
  font-size: 18px;
}

.address-box {
  position: relative;
}

.edit-address-icon {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 20px;
  color: #909399;
  cursor: pointer;
}

.orders-list {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 15px;
  background-color: #fff;
}

.no-orders {
  text-align: center;
  color: #909399;
  padding: 20px;
}

.order-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #ebeef5;
}

.order-item:last-child {
  border-bottom: none;
}

.product-thumbnail {
  width: 60px;
  height: 60px;
  background-color: #f0f2f5;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 15px;
  border-radius: 4px;
  flex-shrink: 0;
}

.order-details {
  display: flex;
  align-items: center;
  flex-grow: 1;
}

.product-info {
  flex-grow: 1;
  text-align: left;
}

.profile-actions {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.order-item-detail {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.product-name {
  font-weight: bold;
  font-size: 15px;
}

.product-quantity {
  font-size: 14px;
  color: #606266;
  margin-left: 10px;
}

.product-price {
  font-size: 14px;
  color: #606266;
}

.order-actions {
  display: flex;
  align-items: center;
  margin-left: 20px;
  flex-shrink: 0;
}

.warning-icon {
  color: #e6a23c;
  font-size: 24px;
  margin-right: 10px;
}

.rate-button {
  width: 80px;
}
</style>