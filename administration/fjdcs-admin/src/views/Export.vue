<template>
    <div class="container is-fluid">
        <div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-export'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">LDIF Export</h1>
        </div>
        <br>
        <div class="columns is-centered">
            <img src="https://cdn.vespotok.net/img/domains-ldif.png">
        </div>

    </div>

</template>
<script>
    export default {
        name: 'Export',
        components: {},
        data() {
            return {
                
            };
        },
        created() {
            this.$parent.activePage = "domainExport";
        },
    }
</script>