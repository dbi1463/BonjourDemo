//
//  BonjourDiscoveredServices.m
//  BonjouriOS
//
//  Created by Spirit on 7/23/14.
//  Copyright (c) 2014 funymph. All rights reserved.
//

#import "BonjourDiscoveredServicesDataSource.h"

#import "BonjourDiscoveredServiceCell.h"

@implementation BonjourDiscoveredServicesDataSource {
	NSArray* _serviceNames;
	NSMutableDictionary* _servcies;
	NSSortDescriptor* _serviceNameSorter;
}

@synthesize delegate;

- (instancetype)init {
	if (self = [super init]) {
		_servcies = [NSMutableDictionary new];
		_serviceNameSorter = [[NSSortDescriptor alloc] initWithKey:@"name" ascending:YES];
	}
	return self;
}

- (void)addService:(NSNetService*)service {
	if (service.name != nil && service.name.length > 0) {
		[_servcies setObject:service forKey:service.name];
		_serviceNames = [[_servcies allKeys] sortedArrayUsingDescriptors:@[ _serviceNameSorter ]];
	}
	[self.delegate discoveredServicesChanged];
}

- (void)removeService:(NSNetService*)service {
	if (service.name != nil && service.name.length > 0) {
		[_servcies removeObjectForKey:service.name];
		_serviceNames = [[_servcies allKeys] sortedArrayUsingDescriptors:@[ _serviceNameSorter ]];
	}
	[self.delegate discoveredServicesChanged];
}

- (NSInteger)tableView:(UITableView*)tableView numberOfRowsInSection:(NSInteger)section {
	return [_servcies count];
}

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(NSIndexPath*)indexPath {
	BonjourDiscoveredServiceCell* cell = (BonjourDiscoveredServiceCell*)[tableView dequeueReusableCellWithIdentifier:BonjourDiscoveredServiceCellIdentifier];
	if (cell == nil) {
		cell = [[[NSBundle mainBundle] loadNibNamed:BonjourDiscoveredServiceCellIdentifier owner:nil options:nil] firstObject];
	}
	NSNetService* service = [_servcies objectForKey:[_serviceNames objectAtIndex:indexPath.row]];
	cell.service = service;
	return cell;
}

@end
