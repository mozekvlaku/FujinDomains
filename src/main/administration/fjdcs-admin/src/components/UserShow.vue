<template>
    <vs-row justify="space-between">
        <vs-tooltip>
            <vs-avatar badge badge-color="success">
                <img :src="profilePic" v-if="picture">
                <template #text v-if="!picture">
                    {{name}}
                </template>
            </vs-avatar>
            <template #tooltip>
                {{name}} <small>({{nt4username}})</small> je přihlášen/a
            </template>
        </vs-tooltip>
        <vs-tooltip>
            <vs-avatar badge-color="danger" @click="logout" badge-position="top-right">
                <i class='bx bx-log-in'></i>
            </vs-avatar><template #tooltip>
                Odhlásit uživatele {{name}} <small>({{nt4username}})</small>
            </template>
        </vs-tooltip>
    </vs-row>
</template>
<script>
    import axios from "axios";
    export default {
        name: 'UserShow',
        components: {},
        props: {},
        data() {
            return {
                name: "",
                nt4username: "",
                token: "",
                domain: "",
                profilePic: "",
                picture: false
            };
        },
        methods: {
            async getLoginData() {

                if (typeof (this.$store.state.token) === undefined || this.$store.state.token === null) {
                    if (typeof (this.$route.query.token) !== undefined && this.$route.query.token !== undefined) {

                        this.$store.commit('LOGIN_SUCCESS', this.$route.query)
                        this.token = this.$route.query.token;
                        this.domain = this.$route.query.domain;
                        const hostname = this.$g('server_url') + "/v1/user?domain=" + this.domain +
                            "&type=console&token=" + this.token;
                        const results = await axios.get(hostname);
                        await new Promise((resolve) => setTimeout(resolve, 500));
                        if (results.data.status === "success") {
                            this.name = results.data.user.cn;
                            this.nt4username = results.data.user.userPrincipalName;
                            this.$store.commit('TOKEN_GET', results.data.user)

                        }
                        if (results.data.user.thumbnailPhoto != null || results.data.user.thumbnailPhoto !=
                            undefined) {
                            this.profilePic = results.data.user.thumbnailPhoto;
                            this.picture = true;
                        }
                        this.$Progress.finish();
                        this.isLoading = false;
                    } else {
                        window.location.href = this.$g('login_window_url') + "?r=" + btoa(window.location.href);
                    }
                } else {
                    this.token = this.$store.state.token
                    this.domain = this.$store.state.domain
                    this.name = this.$store.state.name
                    this.nt4username = this.$store.state.username
                    this.$Progress.finish();
                    this.isLoading = false;

                    if (this.$store.state.thumbnailPhoto != null || this.$store.state.thumbnailPhoto != undefined) {
                        this.profilePic = this.$store.state.thumbnailPhoto;
                        this.picture = true;
                    }
                }

            },
            async logout() {

                const hostname = this.$g('server_url') + "/v1/logout?domain=" + this.domain + "&token=" + this
                .token;
                await axios.get(hostname);
                this.$store.commit('LOGOUT')
                window.location.href = this.$g('login_window_url') + "?r=" + btoa(window.location.href);

            }
        },
        created() {
            this.getLoginData();
        },
    }
</script>