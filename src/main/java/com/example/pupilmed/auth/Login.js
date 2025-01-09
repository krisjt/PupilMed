// ekran logowania
import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {GoHeartFill} from "react-icons/go";
import {IoPaw} from "react-icons/io5";

function Login() {
    const [phone, setPhone] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    // sprawdzenie, czy użytkownik nie jest już zalogowany
    useEffect(() => {
        const auth = JSON.parse(localStorage.getItem("authData"));
        if (auth && auth.role) {
            console.log(auth);
            console.log(auth.role)

            if (auth.role === "ADMIN") {
                navigate("/admin");
            } else if (auth.role === "OWNER") {
                console.log("hi im owner and ill go to /owner")
                navigate("/owner");
            } else if (auth.role === "VET") {
                navigate("/vet");
            }
        }
    }, [navigate]);


    // zalogowanie, jeżeli użytkownik kliknie przycisk "zaloguj się"
    const handleLogin = async () => {
        try {
            const body = {
                username: phone,
                password: password
            };

            const resp = await fetch("http://localhost:8080/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(body)
            });

            if (!resp.ok) {
                setError("Niepoprawny numer telefonu lub hasło");
                return;
            }

            const json = await resp.json();
            console.log(json)
            localStorage.setItem("authData", JSON.stringify(json));

            // Parsowanie danych i sprawdzanie roli
            const authData = JSON.parse(localStorage.getItem("authData"));
            if (authData && authData.role) {
                console.log(authData.role)
                console.log(typeof authData.role)
                if (authData.role === "ADMIN") {
                    navigate("/admin");
                } else if (authData.role === "OWNER") {
                    console.log("hi im owner and ill go to /owner")
                    navigate("/owner");
                } else if (authData.role === "VET") {
                    navigate("/vet");
                } else {
                    setError("Nieznana rola użytkownika");
                }
            } else {
                setError("Niepoprawne dane logowania");
            }
        } catch (error) {
            console.error("Błąd logowania:", error);
            setError("Wystąpił błąd podczas logowania");
        }
    };


    return (
        <div className="app-background-gradient">
            <header className="PupilMed">
                <div className="main-white-box">
                    <p className="big-text">Zaloguj się</p>
                    <p className="big-text">do aplikacji.</p>
                    <div className="login-form">
                        <label htmlFor="phone" className="form-label">
                            Numer telefonu
                        </label>
                        <input
                            type="text"
                            id="phone"
                            className="input-field"
                            placeholder="Wpisz numer telefonu"
                            value={phone}
                            onChange={(e) => setPhone(e.target.value)}
                        />
                        <label htmlFor="password" className="form-label">
                            Hasło
                        </label>
                        <input
                            type="password"
                            id="password"
                            className="input-field"
                            placeholder="Wpisz hasło"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <div className="error-container">
                        <p className="error-message">{error}</p>
                    </div>
                    <button className="login-button2" onClick={handleLogin}>
                        Zaloguj się
                    </button>
                </div>
                <div className="logo-container">
                    <p className="logo-text">PupilMed</p>
                    <div className="heart-with-paw">
                        <GoHeartFill className="heart-icon" />
                        <IoPaw className="paw-icon" />
                    </div>
                </div>
            </header>
        </div>
    );
}


export default Login;