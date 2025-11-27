const storageKey = 'snow'
const snow = document.querySelector('.snow')
const snowflakes = document.querySelectorAll('.snow__flake')
const snowToggle = document.querySelector('.snow-toggle')

function getRndInteger(min, max) {
    return Math.floor(Math.random() * (max - min + 1) ) + min
}

function getRndFloat(min, max) {
    return (Math.random() * (max - min) + min).toFixed(1)
}

snowflakes.forEach(snowflake => {
    snowflake.style.fontSize = getRndFloat(0.7, 1.5) + 'em'
    snowflake.style.animationDuration = getRndInteger(20, 30) + 's'
    snowflake.style.animationDelay = getRndInteger(-1, snowflakes.length / 2) + 's'
})

function changeSnowAnimation(animationName) {
    snow.style.setProperty('--animation-name',  animationName)
}

snowToggle.addEventListener('change', event => {
    changeSnowAnimation(event.target.value)
    localStorage.setItem(storageKey, event.target.value)
})

document.addEventListener('DOMContentLoaded', () => {
    let currentStorage = localStorage.getItem(storageKey)

    if (currentStorage) {
        snowToggle.querySelector(`.snow-toggle__control[value='${currentStorage}']`).checked = true
    }

    changeSnowAnimation(currentStorage)

    window.addEventListener('storage', () => {
        changeSnowAnimation(localStorage.getItem(storageKey))
    })
})

























var decorate = (n, offset, lights) => {
    var decoration, pos = [];

    decoration = Array(n + 1).join("~");

    for(var j = 0; j < lights; j++) pos.push(offset + j);

    var arr = decoration.split("");
    for(var j = 0; j < n; j++){
        if(pos.indexOf(j) > -1) arr[j] = "o";
    }
    decoration = arr.join("");

    return decoration;
}

var indent = (n) => {
    var indents = "";
    for(var i = 0; i < n; i++) indents += " ";
    return indents;
}

var tree = (height, lights) => {
    var branch = "", decoPos = 1, offset = -lights;

    branch += indent(height - 1);
    branch += "@";
    branch += indent(height - 1);
    branch += "";

    for(var i = 1; i <= height; i++){
        branch += indent(height - i, " ");
        branch += decorate(decoPos, offset, lights);
        branch += indent(height - i, " ");

        decoPos += 2;
        offset += 3;
        offset %= decoPos;
        branch += "";
    }

    branch += indent(height - 1);
    branch += "#";
    branch += indent(height - 1);

    return branch;
}


var i = 1;
document.getElementById("tree").innerHTML = tree(20, i++);
setInterval(() => {
    document.getElementById("tree").innerHTML = tree(20, i);
    i %= 4;
    i++;
}, 2500);