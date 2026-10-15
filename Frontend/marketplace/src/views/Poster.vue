<template>
  <div class="poster-page">
    <el-button type="primary" @click="goBack" class="return">← Back</el-button>
    <h2>Eco-Friendly Posters & Events</h2>
    <div class="poster-grid">
      <div v-for="(poster, index) in posters" :key="index" class="poster-card">
        <img :src="poster.image" alt="poster" />
        <h3>{{ poster.title }}</h3>
        <p>{{ poster.description }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Poster1 from '@/assets/Poster1.png'
import Poster2 from '@/assets/Poster2.png'
import Poster3 from '@/assets/Poster3.png'

const router = useRouter()
const goBack = () => {
  router.back()
}

const posters = ref([
  {
    image: Poster1,
    title: 'Recycling Tips',
    description: 'Learn how to properly separate and recycle household waste.',
  },
  {
    image: Poster2,
    title: 'Eco Awareness Event',
    description: 'Join us for a beach cleanup on World Environment Day!',
  },
  {
    image: Poster3,
    title: 'Reusable Products',
    description: 'Switch to reusable bags, bottles, and containers to reduce plastic waste.',
  },
])
</script>

<style scoped>
.return {
  margin-bottom: 20px;
}

.poster-page {
  padding: 20px;
}

.poster-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.poster-card {
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 15px;
  width: 300px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.poster-card img {
  width: 100%;
  height: auto;
  border-radius: 5px;
}
</style>
