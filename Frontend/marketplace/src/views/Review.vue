<template>
  <div>
    <AppHeader />
  </div>
  <div class="close-button">
    <el-icon @click="handleCloseClick">
      <Close />
    </el-icon>
  </div>
  <div class="tabs-container">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="Description" name="description" />
      <el-tab-pane label="Review" name="review" />
    </el-tabs>
  </div>
  <div class="review-view">
    <div class="review-list">
      <div class="review-item" v-for="review in reviews" :key="review.id">
        <div class="review-header"> 
          <span class="username">Customer {{ review.customer_id }}</span>
          <span>
            <el-rate :model-value="review.rating" disabled size="small" show-score="false" />
          </span>
          <span class="date">{{ review.reviewDate }}</span>
        </div>
        <div class="comment">{{ review.comment }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, getCurrentInstance, onMounted } from 'vue';
import { useRouter } from 'vue-router'
import { useAllDataStore } from '@/stores';
import AppHeader from '@/components/AppHeader.vue';

const {proxy} = getCurrentInstance()

const store = useAllDataStore();

const currentProductId = computed(() => store.state.selectedProductId);

const router = useRouter()

const activeTab = ref('review')

const getImageUrl = (user)=>{
    return new URL(`../assets/${user}.png`,import.meta.url).href
}

const reviews = ref([]);

const getAllReviewsByProduct = async () => {
  try{
    const response = await proxy.$api.getAllReviewsByProduct(currentProductId.value);
    reviews.value = response.data.data;
  } catch (error) {
    console.error('Error fetching reviews:', error);
  }
}

const handleTabClick = (tab) => {
  if (tab.props.name === 'description') {
    router.push('/product')
  }
}

const handleCloseClick = () => {
    router.push('/main')
}

onMounted(() => {
  getAllReviewsByProduct();
})
</script>

<style scoped>
.close-button {
    cursor: pointer
}
.review-view {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.section-title {
  font-size: 20px;
  margin-bottom: 20px;
}

.rating-summary {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 30px;
}

.rating-item {
  padding: 5px 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
}

.rating-item:hover {
  background-color: #f5f7fa;
}

.stars {
  margin-right: 5px;
}

.review-item {
  border-bottom: 1px solid #ebeef5;
  padding: 20px 0;
}

.review-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.username {
  font-weight: bold;
}

.date {
  color: #909399;
}

.comment {
  color: #606266;
}
.tabs-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}
.close-button {
  display: flex;
  justify-content: right;
  margin-bottom: 20px;
}
.close-button .el-icon {
  font-size: 25px;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.close-button .el-icon:hover {
  transform: scale(1.1);
}
</style>