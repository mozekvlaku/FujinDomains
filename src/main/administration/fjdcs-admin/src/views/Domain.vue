<template>
    <div class="container is-fluid">
        <div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-cylinder'></i>
            </vs-avatar><img :src="domain.organizationLogo" style="height: 30px;margin-left:15px">

        </div>
        <br>
        <div v-if="!enabled" class="columns is-centered">
            <img src="https://cdn.vespotok.net/img/domains-blocked.png">
        </div>
        <div class="domainshow" v-if="enabled">
            <div class="columns is-vcentered">
                <div class="column"></div>
                <div class="column is-narrow domainshow-title">
                    <h1>{{domain.organizationName}}</h1>
                    <p>Správa vaší domény</p>
                </div>
            </div>
        </div>
        <br>
        <div class="columns is-vcentered" v-if="enabled">
            <div class="column">
                <div class="domainback">
                    <h3 class="is-marginless">Přihlášení uživatelé vaší domény</h3>
                    <br>
                    <div class="columns">
                        <div class="column">
                            <vs-avatar-group max="10" float>
                                <vs-avatar badge v-for="user in domainusers" :key="user.dn" :data="user"
                                    badge-color="success" style="margin-right: 15px">
                                    <img :src="user[1].thumbnailPhoto"
                                        v-if="!(user[1].thumbnailPhoto == '' || user[1].thumbnailPhoto == undefined || user[1].thumbnailPhoto == null)">
                                    <template #text
                                        v-if="user[1].thumbnailPhoto == '' || user[1].thumbnailPhoto == undefined || user[1].thumbnailPhoto == null">
                                        {{user[1].cn}}
                                    </template>
                                </vs-avatar>
                            </vs-avatar-group>
                        </div>
                    </div>
                </div>
            </div>
            <div class="column">
                <div class="domainback">
                    <h3 class="is-marginless">Nastavení domény / organizace</h3>
                    <br>
                    <div class="columns is-vcentered is-multiline">
                        <div class="column is-narrow">
                            <vs-input type="text"
                                v-model="domain.organizationName" label="Jméno vaší organizace"><template #icon>
                                    <i class='bx bx-rename'></i>
                                </template></vs-input>
                        </div>
                        <div class="column is-narrow">
                            <vs-input type="text"
                                v-model="domain.organizationLogo" label="Logo"><template
                                    #icon>
                                    <i class='bx bx-image'></i>
                                </template></vs-input>
                        </div>
                        <div class="column">
                            <vs-button flat @click="saveDomainChanges">Uložit</vs-button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>

</template>
<script>
    import axios from "axios";
    export default {
        name: 'Domains',
        components: {},
        data() {
            return {
                loaded: false,
                domain: {

                },
                domainusers: [],
                style: "",
                enabled: true
            };
        },
        props: {
            domainName: Object
        },
        methods: {
            async loadDomain() {
                const loading = this.$vs.loading({
                    color: 'primary',
                    type: 'corners'
                })
                const hostname = this.$g('server_url') + "/v1/domainlist";
                const results = await axios.get(hostname);
                await new Promise((resolve) => setTimeout(resolve, 500));
                if (results.data.status === "success") {
                    this.domain = results.data.domains[this.domainName.domainName];
                    this.loaded = true;
                    this.$parent.activeDomain = this.domain;
                    this.$parent.searchingDomain = true;
                    this.$parent.activePage = "domainLink";
                    loading.close();
                }
                this.$Progress.finish();
                this.isLoading = false;

            },
            async saveDomainChanges() {
                const loading = this.$vs.loading({
                    color: 'success',
                    type: 'corners'
                })


                var data = new FormData();
                data.append('domain', this.domainName.domainName);
                data.append('auth', this.$store.state.token);
                data.append('data', JSON.stringify(this.domain));
                const hostname = this.$g('server_url') + "/v1/changedomain";
                axios.post(hostname, data, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then((response) => {
                        if (response.data.status === "success") {
                            this.openNotification('top-center', 'success',
                                `<i class='bx bx-select-multiple' ></i>`,
                                'Doména byla úspěšně aktualizována', 'Operace úspěšná');
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
                        this.openNotification('top-center', 'danger',
                            `<i class='bx bx-select-multiple' ></i>`, error, 'Chyba systému');
                    });

            },
            async loadDomainLogins() {
                const loading = this.$vs.loading({
                    color: 'primary',
                    type: 'corners'
                })
                var data = new FormData();
                data.append('domain', this.domainName.domainName);
                data.append('auth', this.$store.state.token);
                const hostname = this.$g('server_url') + "/v1/domainlogins";
                axios.post(hostname, data, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then((response) => {
                        if (response.data.status === "success") {
                            this.domainusers = Object.entries(response.data.data);
                            this.loadDomain();
                            loading.close();
                        } else {
                            this.openNotification('top-center', 'danger',
                                `<i class='bx bx-select-multiple' ></i>`, response.data.message,
                                'Upozornění zabezpečení FJDCS');
                                this.enabled = false
                        }
                        if (response.data === 401) {
                            this.openNotification('top-center', 'danger',
                                `<i class='bx bx-select-multiple' ></i>`, 'K tomuto zdroji nemáte přístup!',
                                'Upozornění zabezpečení FJDCS');
                                this.enabled = false
                        }
                        loading.close();
                    }).catch(function (error) {
                        this.openNotification('top-center', 'danger', `<i class='bx bx-select-multiple' ></i>`,
                            error, 'Chyba systému');
                            this.enabled = false
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
            createFromDN(name) {
                var levelNameArray = name.split(",dc=");
                //remove first dc=
                levelNameArray[0] = levelNameArray[0].substring(3);
                var baseLevelName = levelNameArray[(levelNameArray.length - 2)];
                return baseLevelName.toUpperCase();
            }
        },
        created() {
            this.loadDomainLogins();
            this.$parent.activePage = "domainLink";
        },
    }
</script>