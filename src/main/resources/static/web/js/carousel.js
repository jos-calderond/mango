// carrusel
new Splide("#carrusel_mejores_marcas", {
    perPage: 6,
    breakpoints: {
        768: {
            perPage: 2,
            drag: true,
        },
    },
    gap: 10,
    drag: false,
    arrows: true,
    autoplay: true,
    pagination: true,
    type: "loop",
}).mount();

new Splide("#carrusel_new_in", {
    perPage: 3,
    breakpoints: {
        768: {
            perPage: 1,
            drag: true,
            padding: {
                right: "5rem",
            },
        },
    },
    padding: {
        right: "5rem",
    },
    gap: 10,
    drag: true,
    arrows: false,
    autoplay: false,
    pagination: true,
    type: "loop",
}).mount();

new Splide("#carrusel_en_deporte", {
    perPage: 1,
    breakpoints: {
        450: {
            perPage: 1,
        },
    },
    rewind: true,
    type: "loop",
    autoplay: true,
    pagination: false,
    gap: 10,
}).mount();

new Splide("#carrusel_emprendedores", {
    perPage: 3,
    breakpoints: {
        768: {
            perPage: 1,
            drag: true,
            padding: {
                right: "5rem",
            },
        },
    },
    padding: {
        right: "5rem",
    },
    gap: 10,
    drag: true,
    arrows: false,
    autoplay: false,
    pagination: true,
    type: "loop",
}).mount();
