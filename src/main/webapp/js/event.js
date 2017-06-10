$(document).ready(function() {
	$(".details").on('click', function() {
		$("html, body").animate({
			scrollTop: $(".event-content").offset().top - 100
		}, 500)
	});

    $(".ticket-table tbody").on('click', 'tr', function() {
        var $this = $(this);
        console.log($this);
        $this.find('[name="ticket"]').prop('checked', true);
    });
});	