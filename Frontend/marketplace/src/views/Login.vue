<template>
  <div class="login-container">
    <h1>Login</h1>
    <p>Please enter your username and password</p>

    <el-form :model="form" class="login-form">
      <el-form-item>
        <el-input v-model="form.username" placeholder="Username" />
      </el-form-item>
      <el-form-item>
        <el-input v-model="form.password" type="password" placeholder="Password" />
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="handleLogin" style="width: 100%">Login</el-button>
      </el-form-item>
    </el-form>

    <div class="register-link">
      <router-link to="/register">Register</router-link>
    </div>
  </div>
</template>

<script setup>
import { useAllDataStore } from '@/stores';
import { ref, getCurrentInstance } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const store = useAllDataStore();

const {proxy} = getCurrentInstance()

const router = useRouter();

const form = ref({
  username: '',
  password: ''
});

const handleLogin = async () => {
  try {
    const response = await proxy.$api.login(form.value)
    const token = response.data.data;
    localStorage.setItem('token', token);

    const payloadBase64 = token.split('.')[1];
    const decodedPayload = JSON.parse(atob(payloadBase64));

    const userId = decodedPayload.id;
    store.state.userId = userId;
    const role = decodedPayload.role;
    store.state.userRole = role;

    if (role === 'ADMIN') {
      router.push('/admin/sales');
    } else if (role === 'SELLER') {
      const statusResponse = await proxy.$api.checkSellerStatus(userId);
      const sellerStatus = statusResponse.data.data
      if (sellerStatus) {
        router.push('/seller/product');
      } else {
        router.push('/invalidAccount');
      }
    } else {
      router.push('/main');
    }

  } catch (err) {
    ElMessage({
      message: 'Invalid username or password.',
      type: 'error',
      duration: 1500
    });
  }
};
</script>


<style scoped>
.login-container {
  max-width: 400px;
  margin: 80px auto;
  text-align: center;
}

.login-form {
  margin-top: 20px;
  text-align: left;
}

.register-link {
  margin-top: 10px;
  text-align: right;
}
</style>
