<template>
  <div class="header">
    <div class="navigation-container">
      <el-menu
        class="navigation"
        :default-active="activeMenu"
        mode="horizontal"
        @select="handleSelect"
      >
        <el-menu-item index="sales">Sales</el-menu-item>
        <el-menu-item index="requests">Requests</el-menu-item>
        <el-menu-item index="complaints">Complaints</el-menu-item>
      </el-menu>
    </div>
    <div class="icons">
      <router-link to="/user">
        <el-icon><User /></el-icon>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()

const route = useRoute()

const activeMenu = computed(() => {
  if (route.path.includes('sales')) return 'sales'
  if (route.path.includes('request')) return 'requests'
  if (route.path.includes('complaint')) return 'complaints'
})


const handleSelect = (key) => {
  activeMenu.value = key
  if (key === 'sales') {
    router.push('/admin/sales')
  } else if (key === 'requests') {
    router.push('/admin/request')
  } else if (key === 'complaints') {
    router.push('/admin/complaint')
  }
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 30px;
  height: 60px;
  border-bottom: 1px solid #ddd;
  background-color: #fff;
  position: relative;
}

.navigation-container {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.navigation {
  border-bottom: none;
  margin-bottom: 20px;
  width: 350px;
}

.el-menu-item {
  font-size: 16px;
  padding: 0 20px;
  height: 50px;
  line-height: 50px;
  color: #333;
}

.el-menu-item.is-active {
  color: #409eff;
  border-bottom: 2px solid #409eff;
}

.icons {
  font-size: 24px;
  display: flex;
  align-items: center;
  position: absolute; 
  right: 30px;
  top: 50%;
  transform: translateY(-50%);
}
</style>