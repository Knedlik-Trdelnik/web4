import {createApp} from 'vue'


createApp({
    // Состояние компонента
    data() {
        return {
            isAuthorized: false,
            dots: [],
            rValue: 1, // Пример реактивной переменной для радиуса
            yValue: 0,
            xValue: 0,
            rMessage: '',
            isEvrGood: true,


        }
    },

    watch: {
        yValue(newYValue, oldYValue) {
            if ((newYValue <= -3) || (newYValue >= 3)) {
                this.yValue = oldYValue;
            }
        },

        rValue(newRValue, oldRValue) {
            if (newRValue < 1) {
                this.rMessage = 'Ненормальный радиус? Прикольно, но толку мало'
                this.isEvrGood = false;
            } else {
                this.rMessage = ''
                this.isEvrGood = true;
            }
        }
    },


    methods: {
        drawEvrt() {

        },


        async dotFetch() {
            const dotData = {
                x: this.xValue,
                y: this.yValue,
                r: this.rValue
            }
            try {
                const response = await axios.post('dots', dotData);
                console.log(response.data);
                this.dots.push(response.data);
            } catch (error) {
                console.error('Ошибка входа:', error)
            }


        },
    }
    ,


    mounted() {
        const token = localStorage.getItem("jwt_token");
        if (token) {
            this.isAuthorized = true;
        } else {

            this.isAuthorized = false;
        }

        this.methods.drawEvrt();
    }
}).mount('#app')


