<template>
    <div class="container is-fluid">
        <div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-sitemap'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">Schéma</h1>
            
        </div>
        <div v-if="!enabled" class="columns is-centered">
            <img src="https://cdn.vespotok.net/img/domains-blocked.png">
        </div>
            <div class="columns" v-if="enabled">
                <div class="column is-one-fifth">
                    <v-treeview v-model="schema" :treeTypes="treeTypes" @selected="selected" :openAll="openAll"
                        :contextItems="contextItems"></v-treeview>

                </div>
                <div class="column" v-if="selectedNode.model">
                    <h1 class="title is-marginless">
                        {{selectedNode.model.cn}} <span
                            class="has-text-grey has-text-weight-light">{{selectedNode.model.objectClass}}</span>
                    </h1>
                    <br>
                    <vs-table striped ref="content">
            <template #thead>
                <vs-tr>
                    <vs-th>
                        Jméno
                    </vs-th>
                    <vs-th>
                        Hodnota
                    </vs-th>
                </vs-tr>
            </template>
            <template #tbody>
                <vs-tr :key="tr" v-for="(pair, tr) in selectedArrayOfValues" :data="pair">
                   
                    <vs-td>
                        {{ pair.key }}
                    </vs-td>
                    <vs-td>
                        {{ pair.value }}
                    </vs-td>
                </vs-tr>
            </template>
            <template #notFound>
              Tento objekt je prázdný
            </template>

        </vs-table>
                    
                </div>
            </div>
        </div>
</template>
<script>
    import axios from "axios";
    import VTreeview from "v-treeview"

    export default {
        name: 'Domains',
        components: {
            VTreeview
        },
        data() {
            return {
                enabled: true,
                openAll: true,
                treeTypes: [{
                        type: "#",
                        valid_children: [
                            "LDAP_PERSON",
                            "LDAP_GROUP",
                            "LDAP_OU",
                            "LDAP_CONTAINER",
                            "LDAP_COMPUTER",
                            "LDAP_DC", "LDAP_NAMINGCONTEXT"
                        ]
                    },
                    {
                        type: "LDAP_CONTEXT",
                        icon: "fas fa-server",
                        bxicon: "bx bx-server",
                        valid_children: [
                            "LDAP_PERSON",
                            "LDAP_GROUP",
                            "LDAP_OU",
                            "LDAP_CONTAINER",
                            "LDAP_COMPUTER",
                            "LDAP_DC"
                        ]
                    },
                    {
                        type: "LDAP_PERSON",
                        bxicon: "bx bx-user",
                        icon: "fas fa-user",
                        valid_children: [
                            "LDAP_PERSON",
                            "LDAP_GROUP",
                            "LDAP_OU",
                            "LDAP_CONTAINER",
                            "LDAP_COMPUTER",
                            "LDAP_DC"
                        ]
                    },
                    {
                        type: "LDAP_GROUP",
                        icon: "fas fa-users",
                        bxicon: "bx bx-group",
                        valid_children: [
                            "LDAP_PERSON",
                            "LDAP_GROUP",
                            "LDAP_OU",
                            "LDAP_CONTAINER",
                            "LDAP_COMPUTER",
                            "LDAP_DC"
                        ]
                    },
                    {
                        type: "LDAP_OU",
                        icon: "fas fa-inbox",
                        bxicon: "bx bx-server",
                        valid_children: [
                            "LDAP_PERSON",
                            "LDAP_GROUP",
                            "LDAP_OU",
                            "LDAP_CONTAINER",
                            "LDAP_COMPUTER",
                            "LDAP_DC"
                        ]
                    },
                    {
                        type: "LDAP_CONTAINER",
                        icon: "fas fa-inbox",
                        valid_children: [
                            "LDAP_PERSON",
                            "LDAP_GROUP",
                            "LDAP_OU",
                            "LDAP_CONTAINER",
                            "LDAP_COMPUTER",
                            "LDAP_DC"
                        ]
                    },
                    {
                        type: "LDAP_COMPUTER",
                        icon: "fas fa-laptop",
                        valid_children: [
                            "LDAP_PERSON",
                            "LDAP_GROUP",
                            "LDAP_OU",
                            "LDAP_CONTAINER",
                            "LDAP_COMPUTER",
                            "LDAP_DC"
                        ]
                    }
                ],
                schema: [],
                contextItems: [],
                selectedNode: {},
                selectedArrayOfValues: []
            };
        },
        props: {
            domainName: Object,
            namingContext: Object,
        },
        methods: {
            async loadSchema() {
                const hostname = this.$g('server_url') + "/v1/search?q=&domain=" + this.domainName.domainName +
                    "&auth=" + this.$store.state.token;
                const loading = this.$vs.loading({
                    color: 'primary',
                    type: 'corners'
                })
                const results = await axios.get(hostname);
                await new Promise((resolve) => setTimeout(resolve, 500));
                if (results.data.status === "success") {
                    this.isEmpty = false;
                    this.schema = this.list_to_tree(results.data.data, results.data.namingcontext, this.domainName
                        .domainName);
                } else {
                    this.openNotification('top-center', 'danger',
                        `<i class='bx bx-select-multiple' ></i>`, 'K tomuto zdroji nemáte přístup!', 'Upozornění zabezpečení FJDCS');
                    this.enabled = false

                }
                this.$Progress.finish();
                this.isLoading = false;
                loading.close();
            
            },
            openNotification(position = null, color, icon, text, title) {
                this.$vs.notification({
                    icon,progress: 'auto',
                    color,
                    position,
                    title,
                    text
                })
            },
            getTypeRule(type) {
                var typeRule = this.treeTypes.filter(t => t.type == type)[0];
                return typeRule;
            },
            selected(node) {
                this.selectedNode = node;
                this.selectedArrayOfValues = [];
                var values = (Object.values(this.selectedNode.model));
                var keys = (Object.keys(this.selectedNode.model));
                for(var i = 0; i < values.length; i++)
                {
                    this.selectedArrayOfValues.push({value: values[i], key: keys[i]});
                }
            },
            list_to_tree(list, naming_context, domain) {
                list = Object.values(list)
                var map = {},
                    node, roots = [{
                        dn: naming_context,
                        text: "Default naming context (" + domain + ")",
                        type: "LDAP_CONTEXT",
                        children: []
                    }],
                    i;

                for (i = 0; i < list.length; i += 1) {
                    node = list[i];
                    var dnArray = node.dn.split(",")
                    var thisDn = dnArray[0]
                    var previousDn = dnArray[1]
                    list[i].id = thisDn
                    map[list[i].id] = i;
                    list[i].text = list[i].cn
                    list[i].type = "LDAP_" + list[i].objectClass.toUpperCase()
                    list[i].children = [];

                }

                for (i = 0; i < list.length; i += 1) {
                    node = list[i];
                    dnArray = node.dn.split(",")
                    thisDn = dnArray[0]
                    previousDn = dnArray[1]

                    var namingContext = naming_context.split(",")[0]
                    if (previousDn !== namingContext) {
                        list[map[previousDn]].children.push(node);
                    } else {
                        roots[0].children.push(node);
                    }
                }
                return roots;
            }
        },
        created() {
            this.loadSchema();
                this.$parent.activePage = "domainSchema";
        },
    }
</script>