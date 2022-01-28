<template>
    <div class="container is-fluid">
<div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar primary @click="userAction=true" style="margin-left:15px; cursor:pointer">
                <i class='bx bx-plus'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-grid-alt'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">Domény</h1>
            
        </div>
        
        <vs-table striped ref="content">
            <template #thead>
                <vs-tr>
                    <vs-th style="width: 0px">

                    </vs-th>
                    <vs-th>
                        Logo
                    </vs-th>
                    <vs-th>
                        Organizace
                    </vs-th>
                    <vs-th>
                        Doména
                    </vs-th>
                    <vs-th>
                        NT4 doménové jméno
                    </vs-th>
                    <vs-th>
                        Obor názvů
                    </vs-th>
                </vs-tr>
            </template>
            <template #tbody>
                <vs-tr :key="i" v-for="(domain, i) in domainlist" :data="domain">
                    <vs-td>
                        <vs-button icon color="primary" flat tag="router-link"
                            :to="{ path: '/domain/'+domain.domainName }">
                            <i class='bx bx-window-open'></i>
                        </vs-button>
                    </vs-td>
                    <vs-td width="90">
                        <img :src="domain.organizationLogo " style="width:90px" />
                    </vs-td>
                    <vs-td>
                        {{ domain.organizationName }}
                    </vs-td>
                    <vs-td>
                        {{ domain.domainName }}
                    </vs-td>
                    <vs-td>
                        {{ domain.samDomainName }}
                    </vs-td>
                    <vs-td>
                        {{ domain.dn }}
                    </vs-td>
                </vs-tr>
            </template>
        </vs-table>

    </div>
</template>
<script>
    import axios from "axios";
    export default {
        name: 'Domains',
        components: {},
        data() {
            return {
                isEmpty: true,
                isLoading: true,
                domainlist: []
            };
        },
        methods: {
            async loadDomains() {
                const loading = this.$vs.loading({
                  color: 'primary',
                  type: 'corners'
                })
                const hostname = this.$g('server_url') + "/v1/domainlist";
                const results = await axios.get(hostname);
                await new Promise((resolve) => setTimeout(resolve, 500));
                if (results.data.status === "success") {
                    this.isEmpty = false;
                    this.domainlist = results.data.domains;
                }
                this.$Progress.finish();
                this.isLoading = false;
                loading.close();

            }
        },
        created() {
            this.loadDomains();
            this.$parent.searchingDomain = false;
            this.$parent.activePage = "/domain-list";
        },
    }
</script>