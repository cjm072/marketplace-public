<template>
  <div class="register-container">
    <h1>Register</h1>

    <el-form :model="form" label-position="top" class="form">
      <el-form-item label="Username">
        <el-input v-model="form.username" placeholder="Username" />
      </el-form-item>

      <el-form-item label="Password">
        <el-input v-model="form.password" type="password" placeholder="Password" />
      </el-form-item>

      <el-form-item label="Email">
        <el-input v-model="form.email" placeholder="Email" />
      </el-form-item>

      <el-form-item label="Phone">
        <el-input v-model="form.phone" placeholder="Phone" />
      </el-form-item>

      <template v-if="form.role === 'SELLER'">
        <el-form-item label="Company Name">
          <el-input v-model="form.companyName" placeholder="Company Name" />
        </el-form-item>
      </template>

      <el-form-item>
        <el-radio-group v-model="form.role">
          <el-radio label="CUSTOMER">Customer</el-radio>
          <el-radio label="SELLER">Seller</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item>
        <el-button type="success" @click="register">Register</el-button>
        <router-link to="/login" class="link">Login</router-link>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue'
import { ElMessage } from 'element-plus'

const {proxy} = getCurrentInstance()

const form = ref({
  username: '',
  password: '',
  email: '',
  phone: '',
  role: 'CUSTOMER',
  company: ''
})

const register = async () => {
  try {
    const payload = form.value
    if (payload.role === 'CUSTOMER') {
      delete payload.company
      await proxy.$api.registerCustomer(payload);
    } else {
      await proxy.$api.registerSeller(payload);
    }
    ElMessage.success('Registration successful!')
  } catch (e) {
    ElMessage.error('Registration failed.')
  }
}
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 80px auto;
  text-align: center;
}
.form {
  margin-top: 20px;
}
.link {
  margin-left: 20px;
  font-size: 14px;
}
</style>
