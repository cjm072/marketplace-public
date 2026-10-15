<template>
  <div class="common-layout">
    <el-container>
      <AppHeader />
      <el-main class="main-content">
        <div class="navigation-wrapper">
          <Navigation />
        </div>
        <div>
          <ProductListing />
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue';
import Navigation from '@/components/Navigation.vue';
import ProductListing from '@/components/ProductListing.vue';
</script>

<style scoped>
.common-layout {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.el-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex-grow: 1;
  padding: 20px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.navigation-wrapper {
  width: 100%;
  margin-bottom: 20px;
  max-width: 960px;
  text-align: center;
}

.navigation-wrapper :deep(.el-menu--horizontal) {
  min-width: 350px;
  justify-content: center;
  border-bottom: none;
}

.navigation-wrapper :deep(.el-menu-item) {
  white-space: nowrap; 
  overflow: visible;
  text-overflow: unset;
  padding: 0 20px;
}

.main-content > div:last-child {
    width: 100%;
}
</style>