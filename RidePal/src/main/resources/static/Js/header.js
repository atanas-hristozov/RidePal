
        $(window).scroll(function(){
            let getPositionWindow = $(document).scrollTop();
            if (getPositionWindow > 0) {
                $('.header').addClass('header-scroll');
            }
            else {
                $('.header').removeClass('header-scroll');
            }
        });

 