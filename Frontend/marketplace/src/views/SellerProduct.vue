<template>
    <div>
        <AdminNavigation />
    </div>
    <div>
        <ProductListing />
    </div>
</template>

<script setup>
import ProductListing from '@/components/ProductListing.vue';
import AdminNavigation from '../components/SellerNavigation.vue';
import { useAllDataStore } from '@/stores';
import { onMounted, onUnmounted } from 'vue';

const store = useAllDataStore();

store.setIsAdmin(true);

onUnmounted(() => {
    store.setIsAdmin(false);
});
</script>

<style scoped>
</style>