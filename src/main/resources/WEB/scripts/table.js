import {createApp} from 'vue'



createApp({
    data() {
        return {
            users: [],
            status: "Бездействие..."
        }
    },
    methods: {
        async loadDots() {
            try {
                const response = await axios.get("dots/");
                this.users = response.data;
                console.log("Успешная загрузка: " + response.data);
                this.status = "Точки успешно загружены!"
            } catch (error) {
                console.error(error)
                this.status = error.message;
            }

        }
    }
}).mount('#app');


