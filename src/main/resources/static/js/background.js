document.addEventListener('DOMContentLoaded', function () {
    var images = [
        '/images/form/dog01.jpeg',
        '/images/form/dog02.jpeg',
        '/images/form/dog03.jpg',
        '/images/form/dog04.jpg',
        '/images/form/dog05.jpg',
        '/images/form/dog06.jpg', 
        '/images/form/dog07.jpg',
        '/images/form/dog08.jpeg',
        '/images/form/dog09.jpg'

    ];

    var intervalTime = 5000; // Intervalo de 5 segundos (5000 milissegundos)
    var bodyElement = document.body;

    function changeBackground() {
        var randomImage = images[Math.floor(Math.random() * images.length)];
        bodyElement.style.backgroundImage = 'url(' + randomImage + ')';
    }

    // Iniciar mudança de imagem automaticamente e repetir a cada intervalTime
    changeBackground(); // Primeira mudança imediata ao carregar
    setInterval(changeBackground, intervalTime);
});
