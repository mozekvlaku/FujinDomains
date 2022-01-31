<template>
    <div class="container is-fluid">
        <div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar @click="loadServer()" primary style="margin-left:15px; cursor:pointer">
                <i class='bx bx-refresh'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-server'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">{{server.address}}</h1>

        </div>
        <br>
        <div class="columns is-vcentered">
            <div class="column is-narrow">
                <img src="http://cdn.vespotok.net/img/fujindomains.svg" alt="Domény Fujinu" style="width:200px">
            </div>
            <div class="column is-narrow">
                <h2 class="is-marginless">{{server.version}}</h2>
                <p class="is-marginless">{{server.subversion}}</p>
                <small>Bc. Tomáš Kracík - Vespotok 2022</small>
            </div>
        </div>

        <h3>Přihlášení uživatelé na serveru</h3>
        <div class="columns">
            <div class="column is-narrow">
                <vs-avatar-group max="10" float>
                    <vs-avatar badge v-for="user in server.users" :key="user.dn" :data="user" badge-color="success" style="margin-right: 15px">
                        <img :src="user[1].thumbnailPhoto"  v-if="!(user[1].thumbnailPhoto == '' || user[1].thumbnailPhoto == undefined || user[1].thumbnailPhoto == null)">
                        <template #text v-if="user[1].thumbnailPhoto == '' || user[1].thumbnailPhoto == undefined || user[1].thumbnailPhoto == null">
                            {{user[1].cn}}
                        </template>
                        <template #badge>
                          {{createFromDN(user[1].dn)}}
                        </template>                     
                    </vs-avatar>
                </vs-avatar-group>
            </div>
        </div>


    </div>

</template>
<script>
    import axios from "axios";
    export default {
        name: 'Server',
        components: {},
        data() {
            return {
                loaded: false,
                server: {
                    address: "auth.vespotok.net",
                    version: "Vespotok Fujin Domains 1.0.0",
                    subversion: "Fujin Directory and Credential Provider Service",
                    users : []
                }
            };
        },
        props: {
            domainName: Object
        },
        methods: {
            async loadServer() {
                const loading = this.$vs.loading({
                    color: 'primary',
                    type: 'corners'
                })
                var data = new FormData();
                data.append('domain', this.$store.state.domain);
                data.append('auth', this.$store.state.token);
                const hostname = this.$g('server_url') + "/v1/server";
                axios.post(hostname, data, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then((response) => {
                        if (response.data.status === "success") {
                            this.server.address = response.data.address;
                            this.server.version = response.data.version;
                            this.server.subversion = response.data.subversion;
                            this.server.users = Object.entries(response.data.data);
                            console.log(this.server.users)
                        } else {
                            this.openNotification('top-center', 'danger',
                                `<i class='bx bx-select-multiple' ></i>`, response.data.message,
                                'Upozornění zabezpečení FJDCS');
                        }
                        if (response.data === 401) {
                            this.openNotification('top-center', 'danger',
                                `<i class='bx bx-select-multiple' ></i>`, 'K tomuto zdroji nemáte přístup!',
                                'Upozornění zabezpečení FJDCS');
                        }
                        loading.close();
                    }).catch(function (error) {
                        this.openNotification('top-center', 'danger', `<i class='bx bx-select-multiple' ></i>`, error, 'Chyba systému');
                    });

            },
            openNotification(position = null, color, icon, text, title) {
                this.$vs.notification({
                    icon,
                    progress: 'auto',
                    color,
                    position,
                    title,
                    text
                })
            },
            createFromDN(name)
            {
                var levelNameArray = name.split(",dc=");
                //remove first dc=
                levelNameArray[0] = levelNameArray[0].substring(3);
                var baseLevelName = levelNameArray[(levelNameArray.length-2)];
                return baseLevelName.toUpperCase();
            }
        },
        created() {
            this.$parent.activePage = "server";
            this.loadServer();
        },
    }
</script>