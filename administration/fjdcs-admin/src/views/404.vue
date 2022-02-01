<template>
    <div class="container is-fluid">
        <div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
        </div>
        <br>
        <div class="e404">
            <h1>404</h1>
            <h3>Tato stránka nebyla nalezena</h3>
        </div>
        

    </div>

</template>
<script>
    export default {
        name: 'E404',
        components: {},
        data() {
            return {
            };
        },
        
    }
</script>