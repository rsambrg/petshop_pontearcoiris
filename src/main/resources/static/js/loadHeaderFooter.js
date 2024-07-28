document.addEventListener('DOMContentLoaded', function() {

    function loadHTML(file, elementId) {
        fetch(file)
            .then(response => response.text())
            .then(data => {
                document.getElementById(elementId).innerHTML = data;
            })
            .catch(error => console.error('Erro ao carregar o arquivo HTML:', error));
    }

    function getRole() {
        return fetch('/api/role')
            .then(response => response.json())
            .then(data => data.role)
            .catch(error => {
                console.error('Erro ao verificar a role do usuário:', error);
                return null;
            });
    }

    getRole().then(role => {
        if (role === 'ROLE_ADMIN') {
            loadHTML('/hf/headeradm.html', 'header');
        } else if(role == 'ROLE_USER') {
            loadHTML('/hf/headerlogin.html', 'header');
        } else {
            loadHTML('/hf/header.html', 'header');
        }
    });

    loadHTML('/hf/footer.html', 'footer');
    loadHTML('/hf/footer2.html', 'footer2');
    loadHTML('/hf/footer3.html', 'footer3');
});
