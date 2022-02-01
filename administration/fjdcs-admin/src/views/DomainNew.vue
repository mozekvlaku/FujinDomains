<template>
    <div>

        <div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar primary @click="handleSubmit" style="margin-left:15px; cursor:pointer">
                <i class='bx bx-save'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-plus'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">Přidání nové domény
            </h1>

        </div>
        <br>

            <div class="columns is-multiline" v-if="$store.state.userlevel == 'SERVER'">
                <div class="column is-narrow">
                    <vs-input type="text" @input="handleChange('organizationName')" v-model="newdomain.organizationName"
                        label="Název organizace" ></vs-input>
                </div>
                <div class="column is-narrow">
                    <vs-input type="text" @input="handleChange('organizationLogo')" v-model="newdomain.organizationLogo"
                        label="Logo" ></vs-input>
                </div>
                <div class="column is-narrow">
                    <vs-input type="text" @input="handleChange('domainName')" v-model="newdomain.domainName" label="Doménové jméno" placeholder="domena.tld"></vs-input>
                       
                </div>
                <div class="column is-narrow is-hidden">
                    <vs-switch dark>
                        <template #off>
                            Zabezpečovací skupina
                        </template>
                        <template #on>
                            Distribuční skupina
                        </template>
                    </vs-switch>
                </div>
            </div>

    </div>
</template>
<script>
    import axios from "axios";
    export default {
        name: 'DomainNew',
        components: {},
        data() {
            return {
                newdomain: {
                    
                }
            };
        },
        methods: {
            async handleSubmit() {
                const loading = this.$vs.loading({
                    color: 'success',
                    type: 'corners'
                })


                var data = new FormData();
                data.append('objectclass', 'domain');
                data.append('domain', "BUILTIN");
                data.append('auth', this.$store.state.token);
                data.append('data', JSON.stringify(this.newdomain));
                const hostname = this.$g('server_url') + "/v1/add";
                axios.post(hostname, data, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then((response) => {
                        if (response.data.status === "success") {
                            this.openNotification('top-center', 'success',
                                `<i class='bx bx-select-multiple' ></i>`,
                                'Doména byla úspěšně přidána', 'Operace úspěšná');
                                this.$router.go(-1)
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
            openNotification(position = null, color, icon, text, title) {
                this.$vs.notification({
                    icon,
                    progress: 'auto',
                    color,
                    position,
                    title,
                    text
                })
            }
        },
        created() {
            this.$parent.activePage = "domainNew";
        },
    }
    
</script>