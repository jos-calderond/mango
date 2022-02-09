var app = new Vue({
    el:"#app",
    data:{
        walletInfo: null,
        categories: null,
        productInfo:{},
        errorToats: null,
        successToats: null,
        errorMsg: null,
        successMsg: null,
    },
    methods:{
        getData_product: function(){
            axios.get("/api/products")
                .then((response) => {
                    this.productInfo = response.data;
                    this.categories = [...new Set(this.productInfo.map(p => p.type))];
                })
                .catch((error)=>{
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },

        getData: function(){
            axios.get("/api/exchanges")
                .then((response) => {
                    //get client ifo
                    this.walletInfo = response.data.wallet.points;
                })
                .catch((error)=>{
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },

        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function(){
            axios.post('/api/logout')
                .then(response => window.location.href="/web/index.html")
                .catch(() =>{
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
        performExchange: function(event){
            var button = event.target
            var id = button.getAttribute('id')
            //call the api
            // console.log("Haciendo exchange");
            var payload = [id]
            axios({
                url: '/api/products',
                method: 'post',
                data: payload
              })
              .then((response) => {
                //   console.log(response);
                  this.successMsg = "Product redeemed successfully!";
                  this.successToats.show();
                  setTimeout(this.redirect, 5000);

              })
              .catch((error) => {
                 // your action on error success
                 console.log(error);
                 this.errorMsg = error.response.data;
                 this.errorToats.show();
              })
        },
        redirect: function(){
            window.location.href="/web/exchanges.html";
        }
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.successToats = new bootstrap.Toast(document.getElementById('success-toast'));
        this.getData_product();
        this.getData();
    }
})