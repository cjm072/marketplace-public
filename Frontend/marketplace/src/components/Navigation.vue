<template>
  <el-menu :default-active="selectedTab" mode="horizontal" @select="handleSelect">
    <el-menu-item index="all">All</el-menu-item>
    <el-menu-item index="category">Category</el-menu-item>
    <el-menu-item index="deals">Deals</el-menu-item>
  </el-menu>
</template>

<script setup>
import { useAllDataStore } from '@/stores'
import { computed } from 'vue'

const store = useAllDataStore()

const selectedTab = computed(()=>store.state.selectedTab)

const handleSelect = (newTab) => {
    store.state.selectedTab = newTab
};
</script>

<style scoped>
.el-menu--horizontal {
  border-bottom: none;
  margin-bottom: 20px;
}

.el-menu-item {
  font-size: 16px;
  padding: 0 20px;
  height: 50px;
  line-height: 50px;
  color: #67C23A;
  transition: color 0.3s, border-bottom 0.3s;
}

.el-menu-item:hover {
  color: #67C23A;
  background-color: #f0f9eb;
}

.el-menu-item.is-active {
  color: #67C23A !important;
  border-bottom: 2px solid #67C23A;
  background-color: transparent;
}

</style>