<template>
  <div>
    <AdminNavigation />
  </div>
    <div class="accounts-section">
      <div class="table-header">
        <h3>Seller Request</h3>
      </div>
      <el-table :data="accounts" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="Name" />
        <el-table-column prop="email" label="Email" />
        <el-table-column prop="phone" label="Phone" />
        <el-table-column prop="company" label="Company"/>
        <el-table-column label="Operations" width="300">
          <template #default="scope">
            <el-button size="small" @click="approveAccount(scope.row)">Approve</el-button>
            <el-button size="small" type="danger" @click="rejectAccount(scope.row)">Reject</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="products-section">
      <div class="table-header">
        <h3>Product Request</h3>
      </div>
      <el-table :data="products" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="Name" />
        <el-table-column prop="price" label="Price" />
        <el-table-column prop="category" label="Category" />
        <el-table-column prop="certifications" label="Certifications"/>
        <el-table-column label="Operations" width="300">
          <template #default="scope">
            <el-button size="small" @click="approveProduct(scope.row)">Approve</el-button>
            <el-button size="small" type="danger" @click="rejectProduct(scope.row)">Reject</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import AdminNavigation from '../components/AdminNavigation.vue';

const {proxy} = getCurrentInstance()

const accounts = ref([]);

const products = ref([]);

const getAllRequests = async () => {
  try{
    const response1 = await proxy.$api.getAllPendingSellers();
    accounts.value = response1.data.data;
    const response2 = await proxy.$api.getAllPendingProducts();
    products.value = response2.data.data;
  } catch (error) {
    console.error('Error fetching requests:', error);
  }
}

const approveAccount = async (row) => {
  await proxy.$api.approveSeller(row.id)
  ElMessage.success(`Approved account: ${row.username}`);
  accounts.value = accounts.value.filter(a => a.id !== row.id);
};

const rejectAccount = (row) => {
  ElMessageBox.confirm(
    `Reject account "${row.username}"?`,
    'Warning',
    {
      confirmButtonText: 'Reject',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  ).then( async () => {
    const response = await proxy.$api.rejectSeller(row.id);
    accounts.value = accounts.value.filter(a => a.id !== row.id);
    ElMessage.success(response.data.message);
  });
};

const approveProduct = async (row) => {
  await proxy.$api.approveProduct(row.id);
  ElMessage.success(`Approved product: ${row.name}`);
  products.value = products.value.filter(p => p.id !== row.id);
};

const rejectProduct = (row) => {
  ElMessageBox.confirm(
    `Reject product "${row.name}"?`,
    'Warning',
    {
      confirmButtonText: 'Reject',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  ).then( async () => {
    const response = await proxy.$api.rejectProduct(row.id);
    products.value = products.value.filter(p => p.id !== row.id);
    ElMessage.success(response.data.message);
  });
};

onMounted(async () => {
  getAllRequests();
});
</script>

<style scoped>
.accounts-section,
.products-section {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  margin-bottom: 30px;
}

.table-header {
  margin-bottom: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.el-table {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.el-button {
  margin-right: 8px;
}
</style>