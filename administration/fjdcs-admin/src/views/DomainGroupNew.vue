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
            <h1 style="margin-left:15px">Přidání nové skupiny
            </h1>

        </div>
        <br>

        <h3 class="is-marginless is-paddingless">Obecné</h3>
            <br>
            <div class="columns is-multiline">
                <div class="column is-narrow">
                    <vs-input type="text" @input="handleChange('cn')" v-model="newgroup.cn"
                        label="Název skupiny" />
                </div>
                <div class="column is-narrow">
                    <vs-input type="text" @input="handleChange('comment')" v-model="newgroup.comment"
                        label="Popis" />
                </div>
                <div class="column is-narrow">
                    <vs-input type="text" @input="handleChange('mail')" v-model="newgroup.mail" label="Email">
                        <template #icon>
                            <i class='bx bx-mail-send'></i>
                        </template></vs-input>
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
        name: 'DomainGroupNew',
        components: {},
        data() {
            return {
                newgroup: {
                    
                }
            };
        },
        props: {
            domainName: Object
        },
        methods: {
            async handleSubmit() {
                const loading = this.$vs.loading({
                    color: 'success',
                    type: 'corners'
                })


                var data = new FormData();
                data.append('objectclass', 'group');
                data.append('domain', this.domainName.domainName.domainName);
                data.append('auth', this.$store.state.token);
                data.append('data', JSON.stringify(this.newgroup));
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
                                'Skupina byla úspěšně přidána', 'Operace úspěšná');
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
            this.$parent.activePage = "domainGroupNew";
        },
    }
    
</script>