var gulp 	= require('gulp'),
  	less 	= require('gulp-less'),
  	rename 	= require('gulp-rename'),
    cleanCSS = require('gulp-clean-css');

var paths = {
  styles: {
    src: 'less/**/*.less',
    dest: 'public/css'
  }
};

function styles() {
  return gulp
  	.src(paths.styles.src, {
      sourcemaps: true
    })
	.pipe(less())
	.pipe(rename({
	  basename: 'main',
	  suffix: '.min'
	}))
.pipe(cleanCSS({debug: true}))
.pipe(gulp.dest(paths.styles.dest));
}


function watch() {
  gulp.watch(paths.styles.src, styles);
}

var build = gulp.parallel(styles, watch);

gulp.task(build);
gulp.task('default', build);