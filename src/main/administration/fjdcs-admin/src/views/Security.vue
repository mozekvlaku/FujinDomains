<template>
    <div class="container is-fluid">
        <div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-lock-open-alt'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">Zabezpečení</h1>

        </div>
        <div class="securityshow">
            <div class="columns is-vcentered">
                <div class="column"></div>
                <div class="column is-narrow domainshow-title">
                    <h1>Chraňte svůj doménový účet</h1>
                    <p>Pravidelná změna hesla vás ochrání před nabouráním</p>
                </div>
            </div>
        </div>
        <br>

        <div class="domainback">
            <h3 class="is-marginless">Změna hesla</h3>
            <br>
            <div class="columns is-vcentered is-multiline">
                <div class="column is-narrow">
                    <vs-input type="password" v-model="password.old" label="Staré heslo"><template #icon>
                            <i class='bx bx-lock-open'></i>
                        </template></vs-input>
                </div>
                <div class="column is-narrow">
                    <vs-input type="password" v-model="password.new1" label="Nové heslo"><template #icon>
                            <i class='bx bx-lock'></i>
                        </template></vs-input>
                </div>
                <div class="column is-narrow">
                    <vs-input type="password" v-model="password.new2" label="Opakujte nové heslo"><template #icon>
                            <i class='bx bx-lock'></i>
                        </template></vs-input>
                </div>
                <div class="column">
                    <vs-button flat @click="resetPassword">Uložit</vs-button>
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
                password: {

                }
            };
        },
        methods: {
            async resetPassword() {
                if ((this.password.new1 == this.password.new2) && this.password.old != null) {
                    const loading = this.$vs.loading({
                        color: 'success',
                        type: 'corners'
                    })


                    var data = new FormData();
                    data.append('auth', this.$store.state.token);
                    data.append('data', JSON.stringify(this.password));
                    const hostname = this.$g('server_url') + "/v1/resetpassword";
                    axios.post(hostname, data, {
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        })
                        .then((response) => {
                            if (response.data.status === "success") {
                                this.openNotification('top-center', 'success',
                                    `<i class='bx bx-select-multiple' ></i>`,
                                    'Heslo bylo úspěšně aktualizováno', 'Operace úspěšná');
                            } else {
                                this.openNotification('top-center', 'danger',
                                    `<i class='bx bx-select-multiple' ></i>`, response.data.message,
                                    'Upozornění zabezpečení FJDCS');
                            }
                            if (response.data === 401) {
                                this.openNotification('top-center', 'danger',
                                    `<i class='bx bx-select-multiple' ></i>`,
                                    'K tomuto zdroji nemáte přístup!',
                                    'Upozornění zabezpečení FJDCS');
                            }
                            loading.close();
                        }).catch(function (error) {
                            this.openNotification('top-center', 'danger',
                                `<i class='bx bx-select-multiple' ></i>`, error, 'Chyba systému');
                        });
                }
                else
                {
                    this.openNotification('top-center', 'warn',
                                    `<i class='bx bx-select-multiple' ></i>`,
                                    'Zkontrolujte zda-li jsou nová hesla stejná', 'Pozor');
                }
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
            this.$parent.activePage = "security";
        },
    }
</script>