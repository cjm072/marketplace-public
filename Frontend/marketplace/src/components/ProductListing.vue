<template>
  <div class="page-content">
    <div v-if="selectedTab === 'all'">
      <div v-if="store.state.isAdmin" style="text-align: right; margin-bottom: 20px;">
        <el-button type="primary" @click="addProduct">Add Product</el-button>
      </div>
      <div class="product-grid">
        <div
          v-for="product in filteredProductsAll"
          :key="product.id"
          class="product-card"
          :style="{ cursor: store.state.isAdmin ? 'default' : 'pointer' }"
          @click="!store.state.isAdmin && clickProduct(product.id)"
        >
          <div v-if="store.state.isAdmin" class="admin-buttons-container"> 
            <el-button type="primary" :icon="Edit" circle size="small" @click.stop="editProduct(product.id)"></el-button>
            <el-button type="danger" :icon="Delete" circle size="small" @click.stop="deleteProduct(product.id)"></el-button>
          </div>
          <div class="product-image-container">
            <el-image :src="getImageUrl(product.image)" fit="contain" class="product-image">
              <template #error>
                <div class="image-slot">
                  <el-icon style="font-size: 48px; color: #909399;"><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </div>
          <div class="product-name">{{ product.name }}</div>
          <div class="product-price">RM {{ product.price.toFixed(2) }}</div>
          <div v-if="product.discounts.length !== 0" class="discount-badge-container">
            <div v-for="discount in product.discounts" :key="discount.id" class="discount-badge">
              {{ discount.name }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="selectedTab === 'category'">
      <el-row :gutter="20">
        <el-col :span="4">
          <div class="category-list">
            <div
              v-for="category in availableCategories"
              :key="category"
              :class="{ 'active-category': selectedCategory === category }"
              @click="selectCategory(category)"
              class="category-item"
            >
              {{ category }}
            </div>
          </div>
        </el-col>
        <el-col :span="20">
          <div class="product-grid">
            <div
              v-for="product in filteredProductsCategory"
              :key="product.id"
              class="product-card"
              @click="!store.state.isAdmin && clickProduct(product.id)"
              style="cursor: pointer;"
            >
              <div class="product-image-container">
                <el-image :src="getImageUrl(product.image)" fit="contain" class="product-image">
                  <template #error>
                    <div class="image-slot">
                      <el-icon style="font-size: 48px; color: #909399;"><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
              </div>
              <div class="product-name">{{ product.name }}</div>
              <div class="product-price">RM {{ product.price.toFixed(2) }}</div>
              <div v-if="product.discounts.length !== 0" class="discount-badge-container">
                <div v-for="discount in product.discounts" :key="discount.id" class="discount-badge">
                  {{ discount.name }}
                </div>
              </div>
            </div>
          </div>
          <div v-if="filteredProductsCategory.length === 0" class="no-products-message">
            No products found for this category.
          </div>
        </el-col>
      </el-row>
    </div>

    <div v-if="selectedTab === 'deals'">
      <div class="product-grid">
        <div
          v-for="product in filteredProductsDeals"
          :key="product.id"
          class="product-card"
          @click="!store.state.isAdmin && clickProduct(product.id)"
          style="cursor: pointer;"
        >
          <div class="product-image-container">
            <el-image :src="getImageUrl(product.image)" fit="contain" class="product-image">
              <template #error>
                <div class="image-slot">
                  <el-icon style="font-size: 48px; color: #909399;"><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </div>
          <div class="product-name">{{ product.name }}</div>
          <div class="product-price">RM {{ product.price.toFixed(2) }}</div>
          <div class="discount-badge-container">
            <div v-for="discount in product.discounts" :key="discount.id" class="discount-badge">
              {{ discount.name }}
            </div>
          </div>
        </div>
      </div>
       <div v-if="filteredProductsDeals.length === 0" class="no-products-message">
            No deals available at the moment.
          </div>
    </div>
  </div>

  <el-dialog
    v-model="dialogVisible"
    :title="action === 'add' ? 'Add New Product' : 'Edit Product'"
    width="50%"
  >
    <el-form :model="productForm" label-width="120px" ref="productFormRef">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Name" prop="name">
            <el-input v-model="productForm.name" />
          </el-form-item>
          <el-form-item label="Description" prop="description">
            <el-input type="textarea" v-model="productForm.description" />
          </el-form-item>
          <el-form-item label="Price (RM)" prop="price">
            <el-input type="number" v-model.number="productForm.price" />
          </el-form-item>
          <el-form-item label="Quantity" prop="quantity">
            <el-input type="number" v-model.number="productForm.quantity" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Category" prop="category">
            <el-select v-model="productForm.category" placeholder="Select category">
              <el-option
                v-for="category in availableCategories.filter(cat => cat !== 'All')"
                :key="category"
                :label="category"
                :value="category"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="Image">
            <div style="margin-bottom: 10px;">
              <el-image
                v-if="productForm.image"
                :src="getImageUrl(productForm.image)"
                style="width: 100px; height: 100px;"
                fit="cover"
              />
              <div v-else style="width: 100px; height: 100px; background-color: #f0f0f0; display: flex; align-items: center; justify-content: center;">
                <el-icon><Picture /></el-icon>
              </div>
            </div>
            <input type="file" @change="handleImageUpload" />
          </el-form-item>
          <el-form-item label="Weight (kg)" prop="weight">
            <el-input type="number" v-model.number="productForm.weight" step="0.01"/>
          </el-form-item>
          <el-form-item label="Materials" prop="materials">
            <el-input v-model="productForm.materials" />
          </el-form-item>
          <el-form-item label="Certifications" prop="certifications">
            <el-input v-model="productForm.certifications" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row style="justify-content: flex-end; margin-top: 20px;">
        <el-form-item>
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleSubmit">Save</el-button>
        </el-form-item>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<script setup>
import { useAllDataStore } from '@/stores';
import { ref, getCurrentInstance, computed, onMounted, watch } from 'vue';
import { Picture, Edit, Delete } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router'

const router = useRouter()

const { proxy } = getCurrentInstance()

const store = useAllDataStore();

const products = ref([]);

const filteredProducts = ref([]);

const productForm = ref({
  name: '',
  description: '',
  price: '',
  image: '',
  quantity: null,
  category: '',
  weight: null,
  materials: '',
  certifications: '',
  sellerId: null
});

const dialogVisible = ref(false);
const action = ref('add');
const productFormRef = ref(null);
const sellerId = computed(()=>store.state.userId);

const openDialog = (mode, product = null) => {
  action.value = mode;
  if (mode === 'edit' && product) {
    productForm.value = {
      name: product.name,
      description: product.description,
      price: product.price,
      image: product.image,
      quantity: product.quantity,
      category: product.category,
      weight: product.weight,
      materials: product.materials,
      certifications: product.certifications,
      sellerId: product.sellerId
    };
  } else {
    productForm.value = {
      name: '',
      description: '',
      price: '',
      image: '',
      quantity: null,
      category: '',
      weight: null,
      materials: '',
      certifications: '',
      sellerId: sellerId.value
    };
  }
  dialogVisible.value = true;
};

const uploadedFile = ref(null)

const handleImageUpload = (event) => {
  uploadedFile.value = event.target.files[0]
}

const handleSubmit = async () => {
  try {
    let response;

    if (action.value === 'add') {
      response = await proxy.$api.addProduct(productForm.value);
      selectedProductId.value = response.data.data.id;
    } else {
      await proxy.$api.updateProduct(productForm.value, selectedProductId.value);
    }

    if (uploadedFile.value) {
      await proxy.$api.uploadProductPicture(uploadedFile.value, selectedProductId.value);
    }

    dialogVisible.value = false;
    uploadedFile.value = null;
    getAllProducts();
  } catch (error) {
    console.error('Error saving product:', error);
  }
}

const selectedCategory = ref('All');

const availableCategories = ref([
  'All',
  'Fashion',
  'Kitchen',
  'Travel',
  'Health',
  'Office',
  'PetProduct'
]);

const selectedTab = computed(() => store.state.selectedTab);

const getImageUrl = (imageName) => {
  try {
    return new URL(`/src/assets/${imageName}`, import.meta.url).href;
  } catch (e) {
    console.error(`Image not found: ${imageName}`, e);
    return '';
  }
};

watch(() => store.state.searchQuery, (newQuery) => {
  console.log(store.state.searchQuery)
  searchProducts(newQuery);
});

const searchProducts = async (query) => {
  if (!query || query.trim() === '') {
    filteredProducts.value = products.value;
    return;
  }
  const response = await proxy.$api.getProductsByName(query);
  console.log(response.data.data)
  filteredProducts.value = response.data.data;
};


const getAllProducts = async () => {
  if (store.state.isAdmin === true) {
    const response = await proxy.$api.getAllSellerProducts(sellerId.value)
    products.value = response.data.data
    filteredProducts.value = products.value;
  } else {
    const response = await proxy.$api.getAllApprovedProducts()
    products.value = response.data.data
    filteredProducts.value = products.value;
  }
}

const filteredProductsAll = computed(() => filteredProducts.value);

const filteredProductsCategory = computed(() => {
  if (selectedCategory.value === 'All') {
    return products.value;
  }
  return products.value.filter(product => product.category === selectedCategory.value);
});

const filteredProductsDeals = computed(() => {
  return products.value.filter(product => product.discounts.length !== 0);
});

const selectCategory = (category) => {
  selectedCategory.value = category;
};

const selectedProductId = ref();

const addProduct = async () => {
  openDialog('add');
};

const editProduct = (productId) => {
  const product = products.value.find(p => p.id === productId);
  selectedProductId.value = productId;
  if (product) {
    openDialog('edit', product);
  }
};

const deleteProduct = async (productId) => {
  await proxy.$api.deleteProduct(productId);
  getAllProducts();
};

const clickProduct = (productId) => {
  router.push('/product');
  store.state.selectedProductId = productId;
}

onMounted(() => {
  getAllProducts();
});
</script>

<style scoped>
.page-content {
  padding-top: 20px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.product-card {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 15px;
  text-align: center;
  position: relative;
  background-color: #f9f9f9;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  transition: all 0.3s ease;
}

.product-card:hover {
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  transform: translateY(-3px);
}

.admin-buttons-container {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 10;
  display: flex;
  gap: 5px;
}

.product-image-container {
  width: 100%;
  height: 150px;
  background-color: #dcdfe6;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 10px;
  border-radius: 4px;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 30px;
}

.product-name {
  font-weight: bold;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-price {
  color: #606266;
  font-size: 1.1em;
}

.discount-badge-container {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 10;
  display: flex;
  gap: 5px;
  flex-direction: column;
}

.discount-badge {
  background-color: #67c23a;
  color: white;
  padding: 5px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
  white-space: nowrap;
}


.category-list {
  border-right: 1px solid #ebeef5;
  padding-right: 20px;
  text-align: left;
}

.category-item {
  padding: 10px 0;
  cursor: pointer;
  font-size: 16px;
  color: #606266;
  transition: color 0.2s ease;
}

.category-item:hover {
  color: #409eff;
}

.category-list div.active-category {
  color: #409eff;
  font-weight: bold;
}

.no-products-message {
  text-align: center;
  padding: 50px;
  color: #909399;
  font-size: 18px;
}
</style>
