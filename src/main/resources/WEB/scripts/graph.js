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
                this.onclick(newRValue);
            }
        }
    },


    methods: {
        drawEvrt() {

            const canvas = document.getElementById("graphic");
            const ctx = canvas.getContext("2d");

            ctx.font = "bold 16px Arial";


            ctx.beginPath();
            ctx.fillStyle = "#4864d6";
            ctx.moveTo(200, 200);
            ctx.lineTo(200, 400);

            ctx.moveTo(200, 200);
            ctx.lineTo(200, 0);

            ctx.lineTo(215, 15);
            ctx.moveTo(200, 0);
            ctx.lineTo(185, 15);

            ctx.moveTo(200, 200);
            ctx.lineTo(0, 200);

            ctx.moveTo(200, 200);
            ctx.lineTo(400, 200);

            ctx.lineTo(385, 215);
            ctx.moveTo(400, 200);
            ctx.lineTo(385, 185);
            ctx.closePath();
            ctx.fillStyle = "#4864d6";
            ctx.fillText("Y", 230, 15)
            ctx.fillStyle = "#4864d6";
            ctx.fillText("X", 385, 170)


            //рисуем деления на оси x

            for (let n1 = 40; n1 <= 360; n1 += 40) {
                ctx.moveTo(n1, 200);
                ctx.lineTo(n1, 206);
                ctx.lineTo(n1, 194);

            }
            //рисуем деления на оси y

            for (let n1 = 40; n1 <= 360; n1 += 40) {
                ctx.moveTo(200, n1);
                ctx.lineTo(206, n1);
                ctx.lineTo(194, n1);
            }


            ctx.fillText("-4R", 30, 190)
            ctx.fillText("-3R", 70, 190)
            ctx.fillText("-2R", 110, 190)
            ctx.fillText("-1R", 150, 190)
            ctx.fillText("4R", 350, 190)
            ctx.fillText("3R", 310, 190)
            ctx.fillText("2R", 270, 190)
            ctx.fillText("1R", 230, 190)

            ctx.fillText("4R", 210, 40)
            ctx.fillText("3R", 210, 80)
            ctx.fillText("2R", 210, 120)
            ctx.fillText("1R", 210, 160)
            ctx.fillText("-4R", 210, 360)
            ctx.fillText("-3R", 210, 320)
            ctx.fillText("-2R", 210, 280)
            ctx.fillText("-1R", 210, 240)

            ctx.strokeStyle = 'black';
            ctx.lineWidth = 2;
            ctx.stroke();
        },
        onclick(e) {
            const canvas = document.getElementById("graphic");
            const ctx = canvas.getContext("2d");
            ctx.clearRect(0, 0, canvas.width, canvas.height);

            let r = e;
            ctx.beginPath()

            //треугольник  в 2 четверти
            ctx.moveTo(200 - 40 * r, 200);

            ctx.lineTo(200, 200 - r * 40);
            ctx.lineTo(200, 200)
            ctx.lineTo(200 - 40 * r, 200)

            ctx.fillStyle = "#7bc8f6";
            ctx.fill()
            //четверть круга  в 3 четверти
            ctx.moveTo(200, 200);


            ctx.arc(200, 200, r * 40, Math.PI / 2, Math.PI)
            ctx.moveTo(200, 200);
            ctx.moveTo(200, 200 - 40 * r)
            ctx.fill()

            //квадрат 1 К 1 В 4 четверти

            ctx.lineTo(200, 200);
            ctx.lineTo(200 + r * 40, 200);
            ctx.lineTo(200 + r * 40, 200 + r * 40);
            ctx.lineTo(200, 200 + 40 * r)
            ctx.fill()


            ctx.closePath();
            ctx.strokeStyle = "#7bc8f6";
            ctx.lineWidth = 2;
            ctx.stroke();


            this.drawEvrt();
            //drawPointsWithNewR(r);
            console.log("Все наши точки....");
            console.log(points);
        },

        canvasClick(event) {
            console.log("Обработка клика");
            const canvas = document.getElementById("graphic");
            const ctx = canvas.getContext("2d");
            const rect = canvas.getBoundingClientRect();
            const centerX = 200;
            const centerY = 200;
            const scale = 40;
            const x = (event.clientX - rect.left - centerX) / scale;
            const y = (centerY - (event.clientY - rect.top)) / scale;
            console.log("Отправляем x = " + x +"; y = "+ y + "; r = " + this.rValue );
            this.dotFetchWithArgs(x, y, this.rValue);

        },

        async dotFetchWithArgs(x, y, r) {
            const dotData = {

                x: x,
                y: y,
                r: r
            }
            if (this.isEvrGood) {
                try {
                    const response = await axios.post('dots', dotData);
                    console.log(response.data);
                    this.dots.push(response.data);
                } catch (error) {
                    console.error('Ошибка входа:', error)
                }
            }

        },


        async dotFetch() {
            const dotData = {

                x: this.xValue,
                y: this.yValue,
                r: this.rValue
            }
            if (isEvrGood) {
                try {
                    const response = await axios.post('dots', dotData);
                    console.log(response.data);
                    this.dots.push(response.data);
                } catch (error) {
                    console.error('Ошибка входа:', error)
                }
            }

        },
    },


    mounted() {
        const token = localStorage.getItem("jwt_token");
        if (token) {
            this.isAuthorized = true;
        } else {

            this.isAuthorized = false;
        }
        this.$nextTick(() => {
            this.drawEvrt();
            // если хочешь загрузить уже существующие точки с сервера:
            // axios.get('/dots').then(r => { this.dots = r.data; this.drawEvrt(); })
        });


    }
}).mount('#app')


