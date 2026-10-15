<template>
  <div>
     <SellerNavigation />
  </div>
  <div class="dashboard-container">
    <AnalyticData 
      @date-change="fetchChartData"
    />
    <el-dividedr />
    <div class="discounts-section">
      <div class="table-header">
        <h3>Discounts</h3>
        <div>
          <el-button type="primary" @click="addDiscount">Add Discount</el-button>
          <el-button type="primary" @click="applyDiscount">Apply Discount</el-button>
          <el-button type="primary" @click="removeDiscount">Remove Discount</el-button>
        </div>
      </div>
      <el-table :data="discounts" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="Name" />
        <el-table-column prop="description" label="Description" />
        <el-table-column prop="startDate" label="Start Date" />
        <el-table-column prop="expiryDate" label="Expire Date" />
        <el-table-column prop="amount" label="Amount">
          <template #default="scope">
            RM {{ scope.row.amount }}
          </template>
        </el-table-column>
        <el-table-column label="Operations" width="300">
          <template #default="scope">
            <el-button size="small" @click="editDiscount(scope.row)">Edit</el-button>
            <el-button size="small" type="danger" @click="deleteDiscount(scope.row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
        v-model="isDialogVisible"
        :title="isEditMode ? 'Edit Voucher' : 'Add Voucher'"
        width="500px"
      >
        <el-form :model="discountForm" label-width="120px">
          <el-form-item label="Name">
            <el-input v-model="discountForm.name" />
          </el-form-item>
          <el-form-item label="Description">
            <el-input v-model="discountForm.description" />
          </el-form-item>
          <el-form-item label="Start Date">
            <el-date-picker v-model="discountForm.startDate" type="date" placeholder="Select start date" />
          </el-form-item>
          <el-form-item label="Expire Date">
            <el-date-picker v-model="discountForm.expiryDate" type="date" placeholder="Select expire date" />
          </el-form-item>
          <el-form-item label="Amount (RM)">
            <el-input-number v-model="discountForm.amount" :min="0" step="0.01"/>
          </el-form-item>
        </el-form>
        
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="isDialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="submitDiscount">
              {{ isEditMode ? 'Update' : 'Create' }}
            </el-button>
          </span>
        </template>
      </el-dialog>
      <el-dialog
        v-model="isApplyDialogVisible"c
        title="Apply Discount to Product"
        width="500px"
      >
        <el-form :model="applyForm" label-width="140px">
          <el-form-item label="Select Product">
            <el-select v-model="applyForm.productId" placeholder="Choose a product" style="width: 100%">
              <el-option
                v-for="product in products"
                :key="product.id"
                :label="product.name"
                :value="product.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="Select Discount">
            <el-select v-model="applyForm.discountId" placeholder="Choose a discount" style="width: 100%">
              <el-option
                v-for="discount in discounts"
                :key="discount.id"
                :label="discount.name"
                :value="discount.id"
              />
            </el-select>
          </el-form-item>
        </el-form>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="isApplyDialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="submitApplyDiscount">Apply</el-button>
          </span>
        </template>
      </el-dialog>
      <el-dialog
        v-model="isRemoveDialogVisible"
        title="Remove Discount from Product"
        width="500px"
      >
        <el-form :model="removeForm" label-width="140px">
          <el-form-item label="Select Product">
            <el-select v-model="removeForm.productId" placeholder="Choose a product" style="width: 100%">
              <el-option
                v-for="product in products"
                :key="product.id"
                :label="product.name"
                :value="product.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="Select Discount">
            <el-select v-model="removeForm.discountId" placeholder="Choose a discount" style="width: 100%">
              <el-option
                v-for="discount in discounts"
                :key="discount.id"
                :label="discount.name"
                :value="discount.id"
              />
            </el-select>
          </el-form-item>
        </el-form>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="isRemoveDialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="submitRemoveDiscount">Remove</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useAllDataStore } from '@/stores';
import AnalyticData from '@/components/AnalyticData.vue';
import SellerNavigation from '../components/SellerNavigation.vue';

const {proxy} = getCurrentInstance()

const store = useAllDataStore();

const sellerId = computed(()=>store.state.userId);

const fetchChartData = async () => {
  try {
    const year = store.state.analyticData.selectedYear;
    const month = store.state.analyticData.selectedMonth;

    const [
      countRes,
      amountRes,
      averageRes,
      percentageRes,
      changeRes
    ] = await Promise.all([
      proxy.$api.getSellerPaymentCountPerMonth(year, sellerId.value),
      proxy.$api.getSellerPaymentAmountPerMonth(year, sellerId.value),
      proxy.$api.getSellerAveragePaymentPerMonth(year, sellerId.value),
      proxy.$api.getSellerProductSalesPercentage(year, month, sellerId.value),
      proxy.$api.getSellerProductSalesChange(year, month, sellerId.value)
    ]);

    const orders = countRes.data?.data?.[month] || 0;
    const sales = amountRes.data?.data?.[month] || 0;
    const average = averageRes.data?.data?.[month] || 0;

    const productPercentage = Object.entries(percentageRes.data.data).map(([product, percentage]) => ({
      product,
      percentage: parseFloat(percentage.toFixed(2))
    }));

    const productSalesShare = Object.entries(changeRes.data.data).map(([product, percentage]) => ({
      product,
      salesAmount: parseFloat(percentage.toFixed(2))
    }));

    store.setSalesData({
      orders,
      sales,
      average
    });

    store.setProductData(productPercentage, productSalesShare);
  } catch (error) {
    console.error(error);
    ElMessage.error('Failed to load analytics data');
  }
};

const discounts = ref([]);

const getAllDiscounts = async () => {
  try{
    const response = await proxy.$api.getAllDiscounts(sellerId.value);
    discounts.value = response.data.data;
  } catch (error) {
    console.error('Error fetching discounts:', error);
  }
}

const products = ref([]);

const getAllProducts = async () => {
  try{
    const response = await proxy.$api.getAllSellerProducts(sellerId.value);
    products.value = response.data.data;
  } catch (error) {
    console.error('Error fetching products', error);
  }
}

const isDialogVisible = ref(false);

const isEditMode = ref(false);

const discountForm = ref({
  id: null,
  name: '',
  description: '',
  amount: 0,
  startDate: '',
  expiryDate: '',
  sellerId: sellerId.value
});

const isApplyDialogVisible = ref(false);

const applyForm = ref({
  productId: null,
  discountId: null
});

const isRemoveDialogVisible = ref(false);

const removeForm = ref({
  productId: null,
  discountId: null
});

const selectedDiscount = ref();

const resetForm = () => {
  discountForm.value = {
    id: null,
    name: '',
    description: '',
    amount: 0,
    startDate: '',
    expiryDate: '',
    sellerId: sellerId.value
  };
};

const addDiscount = () => {
  resetForm();
  isEditMode.value = false;
  isDialogVisible.value = true;
};

const editDiscount = (row) => {;
  selectedDiscount.value = row.id
  discountForm.value = { ...row };
  isEditMode.value = true;
  isDialogVisible.value = true;
};

const submitDiscount = async () => {
  if (isEditMode.value) {
    await proxy.$api.updateDiscount(discountForm.value, selectedDiscount.value);
    ElMessage.success('Discount updated successfully');
  } else {
    await proxy.$api.addDiscount(discountForm.value);
    ElMessage.success('New discount added');
  }
  isDialogVisible.value = false;
  getAllDiscounts(sellerId.value);
};

const deleteDiscount = (row) => {
  ElMessageBox.confirm(
    `Delete discount "${row.name}"?`,
    'Warning',
    {
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  ).then( async () => {
    await proxy.$api.deleteDiscount(row.id);
    discounts.value = discounts.value.filter(d => d.id !== row.id);
    ElMessage.success('Discount deleted');
  });
};

const applyDiscount = () => {
  applyForm.value.discountId = null;
  applyForm.value.productId = null;
  isApplyDialogVisible.value = true;
};

const submitApplyDiscount = async () => {
  try {
    await proxy.$api.applyDiscountToProduct(applyForm.value.productId, applyForm.value.discountId);
    ElMessage.success('Discount successfully applied to product');
    isApplyDialogVisible.value = false;
  } catch (error) {
    ElMessage.error('Failed to apply discount');
  }
};

const removeDiscount = () => {
  removeForm.value.productId = null;
  removeForm.value.discountId = null;
  isRemoveDialogVisible.value = true;
};

const submitRemoveDiscount = async () => {
  try {
    await proxy.$api.removeDiscountFromProduct(removeForm.value.productId, removeForm.value.discountId);
    ElMessage.success('Discount removed from product');
    isRemoveDialogVisible.value = false;
  } catch (error) {
    ElMessage.error('Failed to remove discount');
  }
};

onMounted(async () => {
  await fetchChartData();
  getAllDiscounts();
  getAllProducts();
});
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.discounts-section {
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