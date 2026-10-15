<template>
  <div class="invalid-account-container">
    <el-card class="message-card">
      <template #header>
        <div class="header-content">
          <el-icon><Warning /></el-icon>
          <span>Account Not Approved</span>
        </div>
      </template>

      <p>
        Your seller account is currently not approved. Please wait for admin approval or contact support for further assistance.
      </p>

      <el-button type="primary" @click="goBackLogin">Back to Login</el-button>
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { Warning } from '@element-plus/icons-vue';

const router = useRouter();

const goBackLogin = () => {
  router.push('/login');
};
</script>

<style scoped>
.invalid-account-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  background-color: #f5f7fa;
}

.message-card {
  width: 400px;
  text-align: center;
  padding: 20px;
}

.header-content {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
  gap: 10px;
}
</style>
