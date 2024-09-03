function signup() {
    let username = document.getElementById('username').value;
    let password = document.getElementById('password').value;
    let passwordChk = document.getElementById('passwordChk').value;
    let nickname = document.getElementById('nickname').value;
    if (password !== passwordChk) {
        alert('비밀번호가 일치하지 않습니다')
        return
    }
    if (!username) {
        alert('아이디를 입력해주세요.')
        return
    }
    if (!password) {
        alert('비밀번호를 입력해주세요.')
        return
    }
    if (!nickname) {
        alert('닉네임을 입력해주세요.')
        return
    }
    postSignUp({username: username, password: password, nickname: nickname})
}

function postSignUp(req) {
    fetch("/user/signup", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(req)
    }).then(res => res.json()).then(data => {
        if (data) {
            if (!data.success){
                alert(data.message)
                return
            }
            alert("회원가입에 성공했습니다.")
            window.location.href="/account/sign_in"
        }
    })
}