<template>
  <div>
        <AdminNavigation />
  </div>
  <div class="dashboard-container">
    <AnalyticData 
      @date-change="fetchChartData"
    />
    <el-dividedr />
    <div class="vouchers-section">
      <div class="table-header">
        <h3>Vouchers</h3>
        <el-button type="primary" @click="addVoucher">Add Voucher</el-button>
      </div>
      <el-table :data="vouchers" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="Name" />
        <el-table-column prop="description" label="Description" style="width: 100px"/>
        <el-table-column prop="startDate" label="Start Date" />
        <el-table-column prop="expiryDate" label="Expire Date" />
        <el-table-column prop="category" label="Category" />
        <el-table-column prop="amount" label="Amount">
          <template #default="scope">
            RM {{ scope.row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="Quantity" />
        <el-table-column label="Operations" width="300">
          <template #default="scope">
            <el-button size="small" @click="editVoucher(scope.row)">Edit</el-button>
            <el-button size="small" type="danger" @click="deleteVoucher(scope.row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
        v-model="isDialogVisible"
        :title="isEditMode ? 'Edit Voucher' : 'Add Voucher'"
        width="500px"
      >
        <el-form :model="voucherForm" label-width="120px">
          <el-form-item label="Name">
            <el-input v-model="voucherForm.name" />
          </el-form-item>
          <el-form-item label="Description">
            <el-input v-model="voucherForm.description" />
          </el-form-item>
          <el-form-item label="Start Date">
            <el-date-picker v-model="voucherForm.startDate" type="date" placeholder="Select start date" />
          </el-form-item>
          <el-form-item label="Expire Date">
            <el-date-picker v-model="voucherForm.expiryDate" type="date" placeholder="Select expire date" />
          </el-form-item>
          <el-form-item label="Category">
            <el-select v-model="voucherForm.category" placeholder="Select category">
              <el-option v-for="cat in categoryOptions" :key="cat" :label="cat" :value="cat" />
            </el-select>
          </el-form-item>
          <el-form-item label="Amount (RM)">
            <el-input-number v-model="voucherForm.amount" :min="0" step="0.01"/>
          </el-form-item>
          <el-form-item label="Quantity">
            <el-input-number v-model="voucherForm.quantity" :min="0" />
          </el-form-item>
        </el-form>
        
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="isDialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="submitVoucher">
              {{ isEditMode ? 'Update' : 'Create' }}
            </el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useAllDataStore } from '@/stores';
import AnalyticData from '@/components/AnalyticData.vue';
import AdminNavigation from '../components/AdminNavigation.vue';

const store = useAllDataStore();

const {proxy} = getCurrentInstance()

const fetchChartData = async () => {
  try {
    const year = store.state.analyticData.selectedYear;
    const month = store.state.analyticData.selectedMonth;

    const [
      countRes,
      amountRes,
      averageRes,
      percentageRes,
      shareRes
    ] = await Promise.all([
      proxy.$api.getPaymentCountPerMonth(year),
      proxy.$api.getPaymentAmountPerMonth(year),
      proxy.$api.getAveragePaymentPerMonth(year),
      proxy.$api.getCategorySalesPercentage(year, month),
      proxy.$api.getCategorySalesChange(year, month)
    ]);

    const countData = countRes?.data?.data || {};
    const amountData = amountRes?.data?.data || {};
    const averageData = averageRes?.data?.data || {};
    const percentageData = percentageRes?.data?.data || {};
    const shareData = shareRes?.data?.data || {};

    const orders = countData[month] ?? 0;
    const sales = amountData[month] ?? 0;
    const average = averageData[month] ?? 0;

    const productPercentage = Object.entries(percentageData).map(
      ([product, percentage]) => ({
        product: categoryMap[product] ?? product,
        percentage
      })
    );

    const productSalesShare = Object.entries(shareData).map(
      ([product, salesAmount]) => ({
        product: categoryMap[product] ?? product,
        salesAmount
      })
    );

    store.setSalesData({ orders, sales, average });
    store.setProductData(productPercentage, productSalesShare);
  } catch (error) {
    console.error(error);
    ElMessage.error('Failed to load analytics data');
  }
};

const categoryMap = {
  0: 'Fashion',
  1: 'Kitchen',
  2: 'Travel',
  3: 'Health',
  4: 'Office',
  5: 'PetProduct'
};

const categoryOptions = [
  'Fashion',
  'Kitchen',
  'Travel',
  'Health',
  'Office',
  'PetProduct'
];

const vouchers = ref([]);

const getAllVouchers = async () => {
  try{
    const response = await proxy.$api.getAllVouchers();
    vouchers.value = response.data.data;
  } catch (error) {
    console.error('Error fetching vouchers:', error);
  }
}

const isDialogVisible = ref(false);

const isEditMode = ref(false);

const voucherForm = ref({
  id: null,
  name: '',
  description: '',
  startDate: '',
  expiryDate: '',
  amount: 0,
  category: '',
  quantity: 0

});

const selectedVoucher = ref();

const resetForm = () => {
  voucherForm.value = {
    id: null,
    name: '',
    description: '',
    startDate: '',
    expiryDate: '',
    amount: 0,
    category: '',
    quantity: 0
  };
};

const addVoucher = () => {
  resetForm();
  isEditMode.value = false;
  isDialogVisible.value = true;
};

const editVoucher = (row) => {;
  selectedVoucher.value = row.id
  voucherForm.value = { ...row };
  isEditMode.value = true;
  isDialogVisible.value = true;
};

const submitVoucher = async () => {
  if (isEditMode.value) {
    await proxy.$api.updateVoucher(voucherForm.value, selectedVoucher.value);
    ElMessage.success('Voucher updated successfully');
  } else {
    await proxy.$api.addVoucher(voucherForm.value);
    ElMessage.success('New voucher added');
  }
  isDialogVisible.value = false;
  getAllVouchers();
};

const deleteVoucher = (row) => {
  ElMessageBox.confirm(
    `Delete voucher "${row.name}"?`,
    'Warning',
    {
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  ).then( async () => {
    await proxy.$api.deleteVoucher(row.id);
    vouchers.value = vouchers.value.filter(v => v.id !== row.id);
    ElMessage.success('Voucher deleted');
  });
};

onMounted(async () => {
  await fetchChartData();
  getAllVouchers();
});
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.dialog-footer {
  text-align: right;
}

.vouchers-section {
  margin-top: 40px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.el-table {
  margin-top: 20px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-button + .el-button {
  margin-left: 8px;
}
</style>