/*
	Aerial 1.0 by HTML5 UP
	html5up.net | @n33co
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
*/

// Skel.
	skel.init({
		reset: 'full',
		breakpoints: {
			'global': { range: '*', href: 'css/style.css', lockViewport: true, viewport: 'minimal-ui' },
			'wide': { range: '-1680', href: 'css/style-wide.css' },
			'normal': { range: '-1280', href: 'css/style-normal.css' },
			'mobile': { range: '-640', href: 'css/style-mobile.css' },
			'mobilep': { range: '-360', href: 'css/style-mobilep.css' }
		}
	});

// Events (JS).
	
	// Remove "loading" class once the page has fully loaded.
		window.onload = function() {
			document.body.className = '';
		}

	// Prevent scrolling on touch.
		window.ontouchmove = function() {
			return false;
		}

	// Fix scroll position on orientation change.
		window.onorientationchange = function() {
			document.body.scrollTop = 0;
		}

/*

// Events (jQuery).
// Aerial doesn't need jQuery, but if you're going to use it anyway remove the
// block of JS events above and use the jQuery-based ones below instead.

	$(window)

		// Remove "loading" class once the page has fully loaded.
			.on('load', function() {
				$('body').removeClass('loading');
			})

		// Prevent scrolling on touch.
			.on('touchmove', function() {
				return false;
			})

		// Fix scroll position on orientation change.
			.on('orientationchange', function() {
				$('body').scrollTop(0);
			});

*/