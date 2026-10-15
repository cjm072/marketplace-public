<template>
  <div>
    <AdminNavigation />
  </div>
    <div class="complaints-section">
      <div class="table-header">
        <h3>Complaints</h3>
      </div>
      <el-table :data="complaints" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="issue" label="Issue" />
        <el-table-column prop="orderItemId" label="Order Item ID" />
        <el-table-column prop="customerId" label="Customer ID" />
        <el-table-column prop="sellerId" label="Seller ID" />
        <el-table-column label="Operations" width="300">
          <template #default="scope">
            <el-button size="small" @click="dismissComplaint(scope.row)">Dismiss</el-button>
            <el-button size="small" type="danger" @click="deactivateSeller(scope.row)">Deactivate Seller</el-button>
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

const complaints = ref([]);

const getAllComplaints = async () => {
  try{
    const response = await proxy.$api.getAllComplaints();
    complaints.value = response.data.data;
  } catch (error) {
    console.error('Error fetching complaints:', error);
  }
}

const dismissComplaint = (row) => {
  ElMessageBox.confirm(
    `Dismiss complaint "${row.id}"?`,
    'Warning',
    {
      confirmButtonText: 'Dismiss',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  ).then( async () => {
    const response = await proxy.$api.deleteComplaintById(row.id);
    complaints.value = complaints.value.filter(c => c.id !== row.id);
    ElMessage.success(response.data.message);
  });
};

const deactivateSeller = (row) => {
  ElMessageBox.confirm(
    `Deactivate seller "${row.sellerId}"?`,
    'Warning',
    {
      confirmButtonText: 'Deactivate',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  ).then( async () => {
    const response = await proxy.$api.rejectSeller(row.sellerId);
    complaints.value = complaints.value.filter(c=> c.id !== row.id);
    ElMessage.success(response.data.message);
  });
};

onMounted(async () => {
  getAllComplaints();
});
</script>

<style scoped>
.complaints-section {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
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
