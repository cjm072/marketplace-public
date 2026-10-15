<template>
  <el-header class="header">
    <div class="logo">Eco</div>
    <div class="search-bar">
      <el-input v-model="searchText" placeholder="Search..." />
      <el-button icon="Search" @click="doSearch" />
    </div>
    <div class="icons">
      <router-link to="/poster">
        <el-icon><Collection /></el-icon>
      </router-link>
      <router-link to="/user">
        <el-icon><User /></el-icon>
      </router-link>
      <router-link to="/cart">
        <el-icon><ShoppingCart /></el-icon>
      </router-link>
    </div>
  </el-header>
</template>

<script setup>
import { ref } from 'vue'
import { User, ShoppingCart, Search } from '@element-plus/icons-vue';
import { useAllDataStore } from '@/stores';

const store = useAllDataStore();
const searchText = ref('');

const doSearch = () => {
  store.state.searchQuery = searchText.value;
};
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
  border-bottom: 1px solid #ebeef5;
  flex-shrink: 0;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: #67C23A;
}

.search-bar {
  display: flex;
  width: 400px;
}

.search-bar .el-input {
  flex-grow: 1;
}

.search-bar .el-button {
  margin-left: 5px;
}

.icons {
  display: flex;
  gap: 15px;
  font-size: 24px;
  cursor: pointer;
}

.icons a {
  color: inherit;
  text-decoration: none;
}
</style>
